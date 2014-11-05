package com.huawei.esdk.sms.openapi.soap.cxf.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.Base64Utils;
import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.sms.constants.SMSConstants;
import com.huawei.esdk.sms.core.IDispatcher;
import com.huawei.esdk.sms.exception.SDKSMSException;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.openapi.soap.cxf.gen.server.ESGSendIESGtoSMS;
import com.huawei.esdk.sms.openapi.soap.cxf.gen.server.ESGSendSendSMSRequest;
import com.huawei.esdk.sms.openapi.soap.cxf.gen.server.ESGSendSendSMSResponse;
import com.huawei.esdk.sms.utils.SMSUtils;

/**
 * 
 * eSDK短信适配功能SOAP开发接口功能实现
 * 
 * @author  z00209306
 * @see  
 * @since  eSDK Platform SMS V100R003C00
 */
public class ESGSendIESGtoSMSImpl implements ESGSendIESGtoSMS
{
    private static final Logger LOGGER = Logger.getLogger(ESGSendIESGtoSMSImpl.class);
    
    private IDispatcher dispatcher;
    
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
    public ESGSendSendSMSResponse sendSmsRequest(ESGSendSendSMSRequest sendSmsRequest)
    {
        LOGGER.debug("enter into sendSmsRequest");
        ESGSendSendSMSResponse response = new ESGSendSendSMSResponse();
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
    private SMSMessage buildSMSMessage(ESGSendSendSMSRequest sendSmsRequest)
    {
        SMSMessage smsMessage = new SMSMessage();
        smsMessage.setSrcId(sendSmsRequest.getInSExtendCode());
        
        List<String> origMobileNumbers = sendSmsRequest.getInDestinationAddresses().getItem();
        String[] destMobileNumbers = new String[origMobileNumbers.size()];
        int index = 0;
        for (String origMobileNumber : origMobileNumbers)
        {
            destMobileNumbers[index++] = retrieveMobileNumber(origMobileNumber);
        }
        smsMessage.setDestId(destMobileNumbers);
        
        if (!StringUtils.isEmpty(sendSmsRequest.getInSMessage()))
        {
            try
            {
                smsMessage.setContent(new String(Base64Utils.getFromBASE64(sendSmsRequest.getInSMessage()), "UTF-8"));
            }
            catch (UnsupportedEncodingException e)
            {
                LOGGER.error("", e);
            }
        }
        else
        {
            smsMessage.setContent(sendSmsRequest.getInSMessage());
        }
        
        smsMessage.setEncode(SMSConstants.ENCODE_GB2312);
        smsMessage.setNeedReport(sendSmsRequest.getDeliveryResultRequest() == 1 ? true : false);
        
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
    
    public IDispatcher getDispatcher()
    {
        return dispatcher;
    }
    
    public void setDispatcher(IDispatcher dispatcher)
    {
        this.dispatcher = dispatcher;
    }
}
