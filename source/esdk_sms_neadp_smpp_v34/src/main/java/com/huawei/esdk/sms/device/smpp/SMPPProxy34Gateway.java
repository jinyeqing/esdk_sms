package com.huawei.esdk.sms.device.smpp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.BinaryUtils;
import com.huawei.esdk.platform.common.utils.NumberUtils;
import com.huawei.esdk.platform.common.utils.PatternUtils;
import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.sms.constants.SMSConstants;
import com.huawei.esdk.sms.exception.SDKSMSException;
import com.huawei.esdk.sms.model.SMSDeliveryStatus;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.model.SMSStatus;
import com.huawei.esdk.sms.model.SMSubmitRes;
import com.huawei.smproxy.SMPPSMProxy;
import com.huawei.smproxy.comm.smpp.message.SMPPDeliverMessage;
import com.huawei.smproxy.comm.smpp.message.SMPPDeliverRespMessage;
import com.huawei.smproxy.comm.smpp.message.SMPPMessage;
import com.huawei.smproxy.comm.smpp.message.SMPPSubmitMessage;
import com.huawei.smproxy.comm.smpp.message.SMPPSubmitMuitlMessage;
import com.huawei.smproxy.comm.smpp.message.SMPPSubmitMuitlRespMessage;
import com.huawei.smproxy.comm.smpp.message.SMPPSubmitRespMessage;
import com.huawei.smproxy.util.Args;

public class SMPPProxy34Gateway extends SMPPSMProxy implements SMSConstants
{
    private static Logger LOGGER = Logger.getLogger(SMPPProxy34Gateway.class);
    
    private SMSAdapterDeviceSMPP smsAdapterDeviceSMPP;
    
    private SMPPMessageBuilder smppMessageBuilder;
    
    private Pattern deleveryStatusFlagPattern;
    
    private Pattern deleverySMFlagPattern;
    
    public SMPPProxy34Gateway(Args args, SMSAdapterDeviceSMPP smsAdapterDeviceSMPP)
    {
        super(args);
        this.smsAdapterDeviceSMPP = smsAdapterDeviceSMPP;
        init();
    }
    
    public SMPPProxy34Gateway(Map<String, Object> args, SMSAdapterDeviceSMPP smsAdapterDeviceSMPP)
    {
        super(args);
        this.smsAdapterDeviceSMPP = smsAdapterDeviceSMPP;
        init();
    }
    
    private void init()
    {
        smppMessageBuilder = new SMPPMessageBuilder();
        deleveryStatusFlagPattern = Pattern.compile("[0|1]{2}0001[0|1]{2}");
        deleverySMFlagPattern = Pattern.compile("[0|1]{2}0000[0|1]{2}");
    }
    
    public SMSubmitRes sendSms(SMSMessage smsMessage) throws SDKSMSException
    {
        SMSubmitRes submitRes = new SMSubmitRes();
        try
        {
            SMPPMessage responseMessage = null;
            String messageId = null;
            String tempMessageId = null;
            int normalSMMaxLength = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("smpp.sm-max-length", "254"));
            int dataCoding = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("smpp.sm-data-coding", "8"));
            
            if (smsMessage.getDestId().length > 1)
            {
                //群发
                List<SMPPSubmitMuitlMessage> smppSubmitMuitlMessages =
                    smppMessageBuilder.buildSMPPSubmitMuitlMessages(smsMessage, normalSMMaxLength, dataCoding);
                for (SMPPSubmitMuitlMessage smppSubmitMuitlMessage : smppSubmitMuitlMessages)
                {
                    responseMessage = send(smppSubmitMuitlMessage);
                    SMPPSubmitMuitlRespMessage result = (SMPPSubmitMuitlRespMessage)responseMessage;
                    tempMessageId = (result == null ? null : result.getMessageId());
                    LOGGER.debug("Message ID = " + tempMessageId);
                    if (null == messageId)
                    {
                        messageId = tempMessageId;
                    }
                }
            }
            else
            {
                //发单个人
                List<SMPPSubmitMessage> smppMessages =
                    smppMessageBuilder.buildSMPPSubmitMessages(smsMessage, normalSMMaxLength, dataCoding);
                for (SMPPSubmitMessage smppMessage : smppMessages)
                {
                    responseMessage = send(smppMessage);
                    if (null == responseMessage)
                    {
                        continue;
                    }
                    SMPPSubmitRespMessage result = (SMPPSubmitRespMessage)responseMessage;
                    LOGGER.debug("Message ID = " + result.getMessageId());
                    if (null == messageId)
                    {
                        messageId = result.getMessageId();
                    }
                }
            }
            
            if (null != responseMessage && 0 == responseMessage.getStatus())
            {
                submitRes.setResultCode(RESULT_SUCCESS);
                submitRes.setMessageId(messageId);
            }
            else
            {
                String status = (null == responseMessage ? null : String.valueOf(responseMessage.getStatus()));
                LOGGER.info("submit response, messageId" + smsMessage.getSMS4Logging() + ", status=" + status);
                submitRes.setResultCode(RESULT_FAILURE);
                submitRes.setErrorMessage("SMS Send failed and the result code from gateway is " + status);
                return submitRes;
            }
        }
        catch (IllegalArgumentException e)
        {
            LOGGER.error("SMS message send error", e);
            SDKSMSException sdkEx = new SDKSMSException(e.getMessage());
            sdkEx.setSdkErrCode(SMSConstants.SMS_SEND_ERROR);
            throw sdkEx;
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("SMS message send error", e);
            SDKSMSException sdkEx = new SDKSMSException(e.getMessage());
            sdkEx.setSdkErrCode(SMSConstants.SMS_SEND_ERROR);
            throw sdkEx;
        }
        catch (IOException e)
        {
            LOGGER.error("SMS message send error", e);
            SDKSMSException sdkEx = new SDKSMSException(e.getMessage());
            sdkEx.setSdkErrCode(SMSConstants.SMS_SEND_ERROR);
            throw sdkEx;
        }
        
        return submitRes;
    }
    
    @Override
    public SMPPMessage onDeliver(SMPPDeliverMessage deliverMessage)
    {
        String type = BinaryUtils.toBinaryStringWithPadding(deliverMessage.getEsmClass(), 8);
        if (PatternUtils.isMatch(type, deleverySMFlagPattern))
        {
            //x x 0 0 0 0 x x - Default message Type (i.e. normal message) - 上行短信
            SMSMessage smsMessage = new SMSMessage();
            LOGGER.debug("This is a normal message");
            smsMessage.setContent(deliverMessage.getShortMessage());
            smsMessage.setDestId(new String[] {smppMessageBuilder.decodeNumber(deliverMessage.getDestinationAddr())});
            smsMessage.setSrcId(smppMessageBuilder.decodeNumber(deliverMessage.getSourceAddr()));
            PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + smsMessage.getSMS4Logging()
                + "] is arrived from SMPP Gateway");
            smsAdapterDeviceSMPP.onRecvIncomeSMS(smsMessage);
        }
        else if (PatternUtils.isMatch(type, deleveryStatusFlagPattern))
        {
            //x x 0 0 0 1 x x Short Message contains SMSC Delivery Receipt - 状态报告
            LOGGER.debug("This is a deliver receipt");
            SMSDeliveryStatus smsDeliveryStatus = new SMSDeliveryStatus();
            String messageId = "";
            String status = "";
            try
            {
                String shortMessage = deliverMessage.getShortMessage();
                
                shortMessage = new String(shortMessage.getBytes(smppMessageBuilder.getCharset(deliverMessage.getDataCoding())), "UTF-8");
                LOGGER.debug("0-shortMessage = " + shortMessage);
                if (!shortMessage.contains("id:"))
                {
                    shortMessage = new String(deliverMessage.getBytes(), "utf-8");
                    shortMessage = shortMessage.substring(shortMessage.indexOf("id:"));
                    LOGGER.debug("1-shortMessage = " + shortMessage);
                }
                
                int index = shortMessage.toLowerCase(Locale.getDefault()).indexOf("text:");
                if (index > -1)
                {
                    shortMessage = shortMessage.substring(0, index);
                    Map<String, String> tempResult = StringUtils.parseString(shortMessage, " ", ":");
                    messageId = tempResult.get("id");
                    status = tempResult.get("stat");
                }
            }
            catch (UnsupportedEncodingException e)
            {
                LOGGER.error("", e);
            }
            smsDeliveryStatus.setId(messageId);
            List<SMSStatus> statusList = new ArrayList<SMSStatus>(1);
            SMSStatus item = new SMSStatus();
            //Exchange
            item.setSrcMobileNumber(smppMessageBuilder.decodeNumber(deliverMessage.getDestinationAddr()));
            item.setDestMobileNumber(smppMessageBuilder.decodeNumber(deliverMessage.getSourceAddr()));
            item.setStatus(status);
            statusList.add(item);
            smsDeliveryStatus.setStatusList(statusList);
            
            smsAdapterDeviceSMPP.onRecvReceipt(smsDeliveryStatus);
        }
        return new SMPPDeliverRespMessage(0);
    }
}
