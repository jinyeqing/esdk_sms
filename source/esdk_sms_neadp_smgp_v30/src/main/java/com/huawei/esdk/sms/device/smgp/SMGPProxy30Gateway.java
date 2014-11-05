package com.huawei.esdk.sms.device.smgp;

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
import com.huawei.esdk.sms.utils.SMSUtils;
import com.huawei.smproxy.SMGPSMProxy;
import com.huawei.smproxy.comm.smgp.message.SMGPDeliverMessage;
import com.huawei.smproxy.comm.smgp.message.SMGPDeliverRespMessage;
import com.huawei.smproxy.comm.smgp.message.SMGPMessage;
import com.huawei.smproxy.comm.smgp.message.SMGPSubmitMessage;
import com.huawei.smproxy.util.Args;
import com.huawei.smproxy.util.TypeConvert;

public class SMGPProxy30Gateway extends SMGPSMProxy
{
    private static Logger logger = Logger.getLogger(SMGPProxy30Gateway.class);
    
    private SMGPMessageBuilder smgpMessageBuilder;
    
    private SMSAdapterDeviceSMGP smsAdapterDeviceSMGP;
    
    public SMGPProxy30Gateway(Args args, SMSAdapterDeviceSMGP smsAdapterDeviceSMGP)
    {
        super(args);
        this.smsAdapterDeviceSMGP = smsAdapterDeviceSMGP;
        smgpMessageBuilder = new SMGPMessageBuilder();
    }
    
    public SMGPProxy30Gateway(Map<String, Object> args, SMSAdapterDeviceSMGP smsAdapterDeviceSMGP)
    {
        super(args);
        this.smsAdapterDeviceSMGP = smsAdapterDeviceSMGP;
        smgpMessageBuilder = new SMGPMessageBuilder();
    }
    
    public SMSubmitRes sendSms(SMSMessage message) throws SDKSMSException
    {
        SMSubmitRes submitRes = new SMSubmitRes();
        String messageId = null;
        int normalSMMaxLength = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("smgp.sm-max-length", "140"));
        int dataCoding = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("smgp.message.msg.format", "8"));
        
        try
        {
            List<SMGPSubmitMessage> msgList =
                smgpMessageBuilder.buildSMGPSubmitMessages(message, normalSMMaxLength, dataCoding);
            
            for (SMGPSubmitMessage smgpMessage : msgList)
            {
                SMGPMessage responseMessage = send(smgpMessage);
                if (null == responseMessage)
                {
                    continue;
                }
                byte[] resMessageAsByteArray = responseMessage.getBytes();
                messageId = SMSUtils.parserHexByte2Str(resMessageAsByteArray, 4, 13);
                String status = String.valueOf(TypeConvert.byte2int(resMessageAsByteArray, 14));
                logger.debug("submit response, messageId=" + messageId + ", status=" + status);
                if ("0".equals(status))
                {
                    submitRes.setResultCode(SMSConstants.RESULT_SUCCESS);
                    submitRes.setMessageId(messageId);
                }
                else
                {
                    submitRes.setResultCode(SMSConstants.RESULT_FAILURE);
                    logger.info("submit response, messageId=" + messageId + ", result=" + status);
                    submitRes.setErrorMessage("SMS Send failed and the result code from gateway is " + status);
                    return submitRes;
                }
            }
        }
        catch (Exception e)
        {
            logger.error("SMS message send error", e);
            SDKSMSException sdkEx = new SDKSMSException(e.getMessage());
            sdkEx.setSdkErrCode(SMSConstants.SMS_SEND_ERROR);
            throw sdkEx;
        }
        
        return submitRes;
    }
    
    @Override
    public SMGPMessage onDeliver(SMGPDeliverMessage deliverMessage)
    {
        int msgFlag = deliverMessage.getIsReport();
        //接收到消息为上行短信
        if (msgFlag == 0)
        {
            SMSMessage smsMessage = new SMSMessage();
            smsMessage.setSrcId(smgpMessageBuilder.decodeNumber(deliverMessage.getSrcTermID()));
            smsMessage.setDestId(new String[] {smgpMessageBuilder.decodeNumber(deliverMessage.getDestTermID())});
            smsMessage.setEncode("GB2312");
            try
            {
                smsMessage.setId(new String(deliverMessage.getMsgId(), "UTF-8"));
                
                byte[] ba = deliverMessage.getMsgContent();
                if (ba != null)
                {
                    smsMessage.setContent(new String(ba, smgpMessageBuilder.getCharset(deliverMessage.getMsgFormat())));
                }
            }
            catch (UnsupportedEncodingException e)
            {
                logger.error(e.getMessage(), e);
            }
            
            smsMessage.setType(SMSConstants.SM_RECV_DELIVER);
            PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + smsMessage.getSMS4Logging()
                + "] is arrived from SMGP Gateway");
            smsAdapterDeviceSMGP.onRecvIncomeSMS(smsMessage);
        }
        else
        {
            //接收到消息为状态报告
            SMSDeliveryStatus smsDeliveryStatus = new SMSDeliveryStatus();
            byte[] ba = deliverMessage.getMsgContent();
            if (ba != null)
            {
                byte[] tempBa = new byte[10];
                System.arraycopy(ba, 3, tempBa, 0, 10);
                String messageId = SMSUtils.parserHexByte2Str(tempBa, 0, 9);
                smsDeliveryStatus.setId(messageId);
                
                tempBa = new byte[7];
                System.arraycopy(ba, 81, tempBa, 0, 7);
                
                String state;
                try
                {
                    state = new String(tempBa, "UTF-8");
                }
                catch (UnsupportedEncodingException e)
                {
                    state = "Unkown";
                }
                List<SMSStatus> statusList = new ArrayList<SMSStatus>();
                smsDeliveryStatus.setStatusList(statusList);
                SMSStatus item = new SMSStatus();
                statusList.add(item);
                item.setStatus(state);
                //need exchange?
                item.setDestMobileNumber(smgpMessageBuilder.decodeNumber(deliverMessage.getSrcTermID()));
                item.setSrcMobileNumber(smgpMessageBuilder.decodeNumber(deliverMessage.getDestTermID()));
            }
            
            smsAdapterDeviceSMGP.onRecvReceipt(smsDeliveryStatus);
        }
        return new SMGPDeliverRespMessage(deliverMessage.getMsgId(), 0);
    }
}
