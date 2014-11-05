package com.huawei.esdk.sms.openapi.mas.cxf.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.sms.constants.SMSConstants;
import com.huawei.esdk.sms.core.IDispatcher;
import com.huawei.esdk.sms.exception.SDKSMSException;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.openapi.mas.callback.MsgCallbackMAS;
import com.huawei.esdk.sms.openapi.mas.cxf.gen.server.GetReceivedSmsRequest;
import com.huawei.esdk.sms.openapi.mas.cxf.gen.server.GetReceivedSmsResponse;
import com.huawei.esdk.sms.openapi.mas.cxf.gen.server.GetSmsDeliveryStatusRequest;
import com.huawei.esdk.sms.openapi.mas.cxf.gen.server.GetSmsDeliveryStatusResponse;
import com.huawei.esdk.sms.openapi.mas.cxf.gen.server.MasWbsServer;
import com.huawei.esdk.sms.openapi.mas.cxf.gen.server.PolicyException_Exception;
import com.huawei.esdk.sms.openapi.mas.cxf.gen.server.SendSmsRequest;
import com.huawei.esdk.sms.openapi.mas.cxf.gen.server.SendSmsResponse;
import com.huawei.esdk.sms.openapi.mas.cxf.gen.server.ServiceException_Exception;
import com.huawei.esdk.sms.openapi.mas.cxf.gen.server.StartNotificationRequest;
import com.huawei.esdk.sms.openapi.mas.cxf.gen.server.StopNotificationRequest;
import com.huawei.esdk.sms.utils.SMSUtils;

public class MasWbsServerImpl implements MasWbsServer
{
    private static final Logger LOGGER = Logger.getLogger(MasWbsServerImpl.class);
    
    private IDispatcher dispatcher;
    
    private MsgCallbackMAS msgCallbackMAS;
    
    private static Pattern mobilePhoneNumberPattern;
    
    static
    {
        String configPatter = ConfigManager.getInstance().getValue("mobile.phone.number.pattern");
        if (!StringUtils.isEmpty(configPatter))
        {
            mobilePhoneNumberPattern = Pattern.compile(configPatter);
        }
    }
    
    @Override
    public void stopNotification(StopNotificationRequest stopNotificationRequest)
        throws ServiceException_Exception, PolicyException_Exception
    {
        msgCallbackMAS.unRegisterCallback();
    }
    
    @Override
    public GetReceivedSmsResponse getReceivedSms(GetReceivedSmsRequest getReceivedSmsRequest)
        throws ServiceException_Exception, PolicyException_Exception
    {
        return new GetReceivedSmsResponse();
    }
    
    @Override
    public void startNotification(StartNotificationRequest startNotificationRequest)
        throws ServiceException_Exception, PolicyException_Exception
    {
        msgCallbackMAS.registerCallback(startNotificationRequest.getMessageNotification().get(0).getWSURI().get(0));
    }
    
    @Override
    public SendSmsResponse sendSms(SendSmsRequest sendSmsRequest)
        throws ServiceException_Exception, PolicyException_Exception
    {
        LOGGER.debug("enter into sendSmsRequest");
        SendSmsResponse response = new SendSmsResponse();
        SMSMessage smsMessage = buildSMSMessage(sendSmsRequest);
        
        PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + smsMessage.getSMS4Logging() + "] arrived  successfully");
        try
        {
            String identifier = dispatcher.sendSMSMessage(smsMessage);
            response.setRequestIdentifier(identifier);
        }
        catch (SDKSMSException e)
        {
            LOGGER.error("", e);
            String errorMsg = "eSDK SMS Adapter send the SMS failed: " + e.getMessage();
            if (SMSConstants.SMS_SEND_ERROR == e.getSdkErrCode())
            {
                errorMsg = "eSDK hit error when delivering this message to SMS Gateway and will try to resend it.";
            }
            throw new RuntimeException(errorMsg);
        }
        catch (Exception e)
        {
            LOGGER.error("Unexpected Exception", e);
            String errorMsg = "eSDK SMS Adapter Internal Error";
            throw new RuntimeException(errorMsg);
        }
        
        return response;
    }
    
    /**
     * 
     * 构建短信数据模型
     *
     * @param sendSmsRequest 原始号码
     * @return 构建后的短信数据模型
     * @since eSDK Platform SMS V100R003C00
     */
    private SMSMessage buildSMSMessage(SendSmsRequest sendSmsRequest)
    {
        SMSMessage smsMessage = new SMSMessage();
        smsMessage.setSrcId(sendSmsRequest.getExtendCode());
        
        List<String> origMobileNumbers = sendSmsRequest.getDestinationAddresses();
        String[] destMobileNumbers = new String[origMobileNumbers.size()];
        int index = 0;
        for (String origMobileNumber : origMobileNumbers)
        {
            destMobileNumbers[index++] = retrieveMobileNumber(origMobileNumber);
        }
        smsMessage.setDestId(destMobileNumbers);
        smsMessage.setContent(sendSmsRequest.getMessage());       
        smsMessage.setEncode(SMSConstants.ENCODE_GB2312);
        smsMessage.setNeedReport(sendSmsRequest.isDeliveryResultRequest());
        
        return smsMessage;
    }
    
    /**
     * 
     * 剥离出手机号码
     *
     * @param origMobileNumber 原始号码
     * @return 剥离后的手机号码
     * @since eSDK Platform SMS V100R003C00
     */
    private String retrieveMobileNumber(String origMobileNumber)
    {
        String result = null;
        if (null != mobilePhoneNumberPattern)
        {
            Matcher matcher = mobilePhoneNumberPattern.matcher(origMobileNumber);
            
            while (matcher.find())
            {
                result = matcher.group(0);
                break;
            }
            if (null == result)
            {
                result = origMobileNumber;
            }
        }
        else
        {
            //剥离tel:
            result = SMSUtils.decodeNumberPrefix(SMSConstants.MAS_NUMBER_PREFIX, origMobileNumber);
        }
        
        return result;
    }
    
    @Override
    public GetSmsDeliveryStatusResponse getSmsDeliveryStatus(GetSmsDeliveryStatusRequest getSmsDeliveryStatusRequest)
        throws ServiceException_Exception, PolicyException_Exception
    {
        return new GetSmsDeliveryStatusResponse();
    }
    
    public IDispatcher getDispatcher()
    {
        return dispatcher;
    }
    
    public void setDispatcher(IDispatcher dispatcher)
    {
        this.dispatcher = dispatcher;
    }
    
    public MsgCallbackMAS getMsgCallbackMAS()
    {
        return msgCallbackMAS;
    }
    
    public void setMsgCallbackMAS(MsgCallbackMAS msgCallbackMAS)
    {
        this.msgCallbackMAS = msgCallbackMAS;
    }
}
