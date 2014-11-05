package com.huawei.esdk.sms.device.cmpp;

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
import com.huawei.smproxy.CMPPSMProxy30;
import com.huawei.smproxy.comm.cmpp.message.CMPPMessage;
import com.huawei.smproxy.comm.cmpp30.message.CMPP30DeliverMessage;
import com.huawei.smproxy.comm.cmpp30.message.CMPP30DeliverRepMessage;
import com.huawei.smproxy.comm.cmpp30.message.CMPP30SubmitMessage;
import com.huawei.smproxy.util.Args;
import com.huawei.smproxy.util.TypeConvert;

public class CMPPProxy30Gateway extends CMPPSMProxy30
{
    private static Logger LOGGER = Logger.getLogger(CMPPProxy30Gateway.class);
    
    private CMPPMessageBuilder cmppMessageBuilder;
    
    private SMSAdapterDeviceCMPP smsAdapterDeviceCMPP;
    
    public CMPPProxy30Gateway(Args args, SMSAdapterDeviceCMPP smsAdapterDeviceCMPP)
    {
        super(args);
        this.smsAdapterDeviceCMPP = smsAdapterDeviceCMPP;
        init();
    }
    
    public CMPPProxy30Gateway(Map<String, Object> args, SMSAdapterDeviceCMPP smsAdapterDeviceCMPP)
    {
        super(args);
        this.smsAdapterDeviceCMPP = smsAdapterDeviceCMPP;
        init();
    }
    
    private void init()
    {
        cmppMessageBuilder = new CMPPMessageBuilder();
    }
    
    public SMSubmitRes sendSms(SMSMessage message) throws SDKSMSException
    {
        SMSubmitRes submitRes = new SMSubmitRes();
        try
        {
            int normalSMMaxLength = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("cmpp.sm.max.length", "140"));
            int dataCoding = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("cmpp.message.msg.fmt", "15"));
            List<CMPP30SubmitMessage> msgList =
                cmppMessageBuilder.buildCMPP30SubmitMessages(message, normalSMMaxLength, dataCoding);
            
            CMPPMessage resMessage;
            for (CMPP30SubmitMessage cmppMessage : msgList)
            {
                resMessage = send(cmppMessage);
                if (null == resMessage)
                {
                    continue;
                }
                byte[] resMessageAsByteArray = resMessage.getBytes();
                byte[] messageIdByte = new byte[8];
                System.arraycopy(resMessageAsByteArray, 4, messageIdByte, 0, 8);
                String messageId = String.valueOf(TypeConvert.byte2int(messageIdByte));
                
                String result = String.valueOf(TypeConvert.byte2int(resMessageAsByteArray, 12));
                
                LOGGER.debug("submit response, messageId=" + messageId + ", result=" + result);
                if ("0".equals(result))
                {
                    submitRes.setResultCode(SMSConstants.RESULT_SUCCESS);
                    submitRes.setMessageId(messageId);
                }
                else
                {
                    LOGGER.info("submit response, messageId=" + messageId + ", result=" + result);
                    submitRes.setResultCode(SMSConstants.RESULT_FAILURE);
                    submitRes.setErrorMessage("SMS Send failed and the result code from gateway is " + result);
                    return submitRes;
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("SMS message send error", e);
            SDKSMSException sdkEx = new SDKSMSException(e.getMessage());
            //TODO May not need to re-send
            sdkEx.setSdkErrCode(SMSConstants.SMS_SEND_ERROR);
            throw sdkEx;
        }
        
        return submitRes;
    }
    
    @Override
    public CMPPMessage onDeliver(CMPP30DeliverMessage deliverMessage)
    {
        int msgFlag = deliverMessage.getRegisteredDeliver();
        if (msgFlag == 0)
        {
            //接收到消息为上行短信
            SMSMessage smsMessage = new SMSMessage();
            smsMessage.setSrcId(cmppMessageBuilder.decodeNumber(deliverMessage.getSrcterminalId()));
            smsMessage.setDestId(new String[] {cmppMessageBuilder.decodeNumber(deliverMessage.getDestnationId())});
            smsMessage.setEncode("GB2312");
            smsMessage.setType(SMSConstants.SM_RECV_DELIVER);
            try
            {
                byte[] ba = deliverMessage.getMsgContent();
                if (ba != null)
                {
                    smsMessage.setContent(new String(ba, cmppMessageBuilder.getCharset(deliverMessage.getMsgFmt())));
                }
            }
            catch (UnsupportedEncodingException e)
            {
                LOGGER.error(e.getMessage(), e);
            }
            PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + smsMessage.getSMS4Logging()
                + "] is arrived from CMPP Gateway");
            smsAdapterDeviceCMPP.onRecvIncomeSMS(smsMessage);
        }
        else
        {
            //接收到消息为状态报告
            SMSDeliveryStatus smsDeliveryStatus = new SMSDeliveryStatus();
            List<SMSStatus> statusList = new ArrayList<SMSStatus>();
            smsDeliveryStatus.setStatusList(statusList);
            SMSStatus item = new SMSStatus();
            statusList.add(item);
            item.setStatus(deliverMessage.getStat());
            item.setDestMobileNumber(cmppMessageBuilder.decodeNumber(deliverMessage.getSrcterminalId()));
            item.setSrcMobileNumber(cmppMessageBuilder.decodeNumber(deliverMessage.getDestnationId()));
            String messageId;
            byte[] msgIdChars = deliverMessage.getStatusMsgId();
            if (null != msgIdChars)
            {
                messageId = String.valueOf(TypeConvert.byte2int(msgIdChars));
            }
            else
            {
                messageId = "";
            }
            smsDeliveryStatus.setId(messageId);
            LOGGER.debug("messageId=" + messageId + ",state=" + deliverMessage.getStat());
            smsAdapterDeviceCMPP.onRecvReceipt(smsDeliveryStatus);
        }
        return new CMPP30DeliverRepMessage(deliverMessage.getMsgId(), 0);
    }
}
