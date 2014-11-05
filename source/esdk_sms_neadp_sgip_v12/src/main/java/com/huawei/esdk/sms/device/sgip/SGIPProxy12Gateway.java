package com.huawei.esdk.sms.device.sgip;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.NumberUtils;
import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.sms.constants.SMSConstants;
import com.huawei.esdk.sms.exception.SDKSMSException;
import com.huawei.esdk.sms.model.SMSDeliveryStatus;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.model.SMSStatus;
import com.huawei.esdk.sms.model.SMSubmitRes;
import com.huawei.esdk.sms.utils.DateUtils;
import com.huawei.esdk.sms.utils.SMSUtils;
import com.huawei.smproxy.SGIPSMProxy;
import com.huawei.smproxy.comm.sgip.message.SGIPDeliverMessage;
import com.huawei.smproxy.comm.sgip.message.SGIPDeliverRepMessage;
import com.huawei.smproxy.comm.sgip.message.SGIPMessage;
import com.huawei.smproxy.comm.sgip.message.SGIPReportMessage;
import com.huawei.smproxy.comm.sgip.message.SGIPReportRepMessage;
import com.huawei.smproxy.comm.sgip.message.SGIPSubmitMessage;
import com.huawei.smproxy.comm.sgip.message.SGIPSubmitRepMessage;
import com.huawei.smproxy.util.Args;

public class SGIPProxy12Gateway extends SGIPSMProxy
{
    private static Logger LOGGER = Logger.getLogger(SGIPProxy12Gateway.class);
    
    private SGIPMessageBuilder sgipMessageBuilder;
    
    private SMSAdapterDeviceSGIP smsAdapterDeviceSGIP;
    
    public SGIPProxy12Gateway(Args args, SMSAdapterDeviceSGIP smsAdapterDeviceSGIP)
    {
        super(args);
        this.smsAdapterDeviceSGIP = smsAdapterDeviceSGIP;
        sgipMessageBuilder = new SGIPMessageBuilder();
    }
    
    public SGIPProxy12Gateway(Map<String, Object> args, SMSAdapterDeviceSGIP smsAdapterDeviceSGIP)
    {
        super(args);
        this.smsAdapterDeviceSGIP = smsAdapterDeviceSGIP;
        sgipMessageBuilder = new SGIPMessageBuilder();
    }
    
    public SMSubmitRes sendSms(SMSMessage message) throws SDKSMSException
    {
        LOGGER.debug("sendSms start");
        SMSubmitRes submitRes = new SMSubmitRes();
        int normalSMMaxLength = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("sgip.sm.max.length", "140"));
        int dataCoding = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("sgip.message.messagecoding", "15"));
        try
        {
            List<SGIPSubmitMessage> submitList =
                sgipMessageBuilder.buildSGIPSubmitMessages(message, normalSMMaxLength, dataCoding);
            SGIPSubmitRepMessage resp;
            for (SGIPSubmitMessage submit : submitList)
            {
                resp = (SGIPSubmitRepMessage)send(submit);
                if (null == resp)
                {
                    continue;
                }
                if (resp.getResult() != 0)
                {
                    // 多条消息错误一条就当做是出错
                    LOGGER.debug("Sending SMS to SGIPAgent failed : " + resp.getResult());
                    submitRes.setResultCode(SMSConstants.RESULT_FAILURE);
                    submitRes.setMessageId(String.valueOf(resp.getSequenceId()));
                    submitRes.setErrorMessage("SMS Send failed and the result code from gateway is " + resp.getResult());
                    return submitRes;
                }
                
                // 都没有错误的时候发送正常
                LOGGER.debug("Sending SMS to SGIPAgent successed : " + resp.getResult());
                submitRes.setResultCode(SMSConstants.RESULT_SUCCESS);
                submitRes.setMessageId(String.valueOf(resp.getSequenceId()));
                return submitRes;
            }
        }
        catch (Exception e)
        {
            LOGGER.error("Sending SMS to SGIPAgent error : " + e.getMessage(), e);
            SDKSMSException sdkEx = new SDKSMSException(e.getMessage());
            sdkEx.setSdkErrCode(SMSConstants.SMS_SEND_ERROR);
            throw sdkEx;
        }
        
        return submitRes;
    }
    
    @Override
    public SGIPMessage onDeliver(SGIPDeliverMessage msg)
    {
        if (msg != null)
        {
            LOGGER.debug("Received SGIP uplink SMS for " + msg.getSPNumber() + " at "
                + DateUtils.getCurrentDateAsString(DateUtils.DT_FT_YYYYMMDDHHMISSSSS));
            
            SMSMessage message = new SMSMessage();
            try
            {
                message.setContent(new String(msg.getMsgContent(), sgipMessageBuilder.getCharset(msg.getMsgFmt())));
            }
            catch (UnsupportedEncodingException e)
            {
                try
                {
                    message.setContent(new String(msg.getMsgContent(), "UTF-8"));
                }
                catch (UnsupportedEncodingException e1)
                {
                    message.setContent("");
                }
            }

            message.setId(String.valueOf(msg.getSequenceId()));
            message.setSrcId(sgipMessageBuilder.decodeNumber(msg.getUserNumber()));
            message.setDestId(new String[] {sgipMessageBuilder.decodeNumber(msg.getSPNumber())});
            
            message.setEncode("GB2312");
            PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + message.getSMS4Logging()
                + "] is arrived from SGIP Gateway");
            smsAdapterDeviceSGIP.onRecvIncomeSMS(message);
        }
        
        return new SGIPDeliverRepMessage(0);
    }
    
    /**
     * 处理接到的状态报告
     * 
     * @param msg 收到状态报告
     * @return 返回收到的状态报告
     */
    public SGIPMessage onReport(SGIPReportMessage msg)
    {
        if (msg != null)
        {
            // 交给enterprise的适配器处理发送给产品的消息
            SMSDeliveryStatus smsDeliveryStatus = new SMSDeliveryStatus();
            try
            {
                smsDeliveryStatus.setId(new String(msg.getSubmitSequenceNumber(), "UTF-8"));
            }
            catch (UnsupportedEncodingException e)
            {
                smsDeliveryStatus.setId("");
            }

            List<SMSStatus> statusList = new ArrayList<SMSStatus>();
            smsDeliveryStatus.setStatusList(statusList);
            SMSStatus item = new SMSStatus();
            statusList.add(item);
            item.setStatus(getState(msg.getState()));
            item.setDestMobileNumber(SMSUtils.decodeNumberPrefix(SMSConstants.COUNTRY_CODE, msg.getUserNumber()));
            item.setSrcMobileNumber("");
            
            smsAdapterDeviceSGIP.onRecvReceipt(smsDeliveryStatus);
        }
        
        return new SGIPReportRepMessage(0);
    }
    
    private String getState(int i)
    {
        String result;
        if (0 == i)
        {
            result = "DELIVRD";
        }
        else if (1 == i)
        {
            result = "PENDING";//MessageWaiting
        }
        else if (2 == i)
        {
            result = "FAILED";//DeliveryImpossible
        }
        else
        {
            result = "UNKNOWN";//DeliveryUncertain
        }
        
        return result;
    }
}
