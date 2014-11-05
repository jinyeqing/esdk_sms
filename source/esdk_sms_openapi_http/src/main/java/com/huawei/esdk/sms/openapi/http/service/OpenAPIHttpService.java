package com.huawei.esdk.sms.openapi.http.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.ThreadLocalHolder;
import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.AESCbc128Utils;
import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.platform.common.utils.encryption.MD5Utils;
import com.huawei.esdk.sms.constants.SMSConstants;
import com.huawei.esdk.sms.core.IDispatcher;
import com.huawei.esdk.sms.exception.SDKSMSException;
import com.huawei.esdk.sms.model.SMSMessage;

public class OpenAPIHttpService
{
    private static final Logger LOGGER = Logger.getLogger(OpenAPIHttpService.class);
    
    private IDispatcher dispatcher;

    private String eSDKUser;
    
    private String eSDKPass;
    
    public OpenAPIHttpService()
    {
        eSDKUser = ConfigManager.getInstance().getValue("esdk.sms.http.server.login.name");
        eSDKPass = ConfigManager.getInstance().getValue("esdk.sms.http.server.password");
        //To decrypt the password
        eSDKPass = AESCbc128Utils.decryptPwd(eSDKUser, eSDKPass);
    }
    
    public boolean authenticateUser(String userName, String password)
    {
        if (StringUtils.isNotEmpty(userName) && userName.equals(eSDKUser))
        {
            if (StringUtils.isNotEmpty(password)
                && password.equalsIgnoreCase(MD5Utils.do32BitMD5(eSDKPass)))
            {
                return true;
            }
        }
        
        LOGGER.warn("User authentication failed, userName=" + userName);
        
        return false;
    }
    
    public String processSendSmsRequest(String userName, String password, String encode,
            String to, String content)
    {
        String resultCode;
        LOGGER.debug("enter into processSendSmsRequest");
        if (!authenticateUser(userName, password))
        {
            return "-2";
        }
        
        if (!validateParams(encode, to, content))
        {
            //TODO: Refine the error code
            return "-1";
        }
        
        SMSMessage smsMessage = buildSMSMessage(userName, password, encode, to, content);
        
        PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + smsMessage.getSMS4Logging() + "] arrived  successfully from HTTP service.");
        try
        {
            String identifier = dispatcher.sendSMSMessage(smsMessage);
            LOGGER.debug("The message identifier is " + identifier);
            resultCode = (String)ThreadLocalHolder.get().getEntities().get("resultCode");
        }
        catch (SDKSMSException e)
        {
            String errorMsg = "eSDK SMS Adapter send the SMS failed: " + e.getSdkErrCode();
            LOGGER.error(errorMsg, e);
            if (SMSConstants.SMS_SEND_ERROR == e.getSdkErrCode())
            {
                errorMsg = "eSDK hit error when delivering this message to SMS Gateway and will try to resend it.";
            }
            resultCode = "-1";
        }
        catch (Exception e)
        {
            LOGGER.error("Unexpected Exception", e);
            resultCode = "-1";
        }
        
        return resultCode;
    }
    
    private boolean validateParams(String encode, String to, String content)
    {
        if (StringUtils.isEmpty(to))
        {
            LOGGER.warn("Destination number is emtpy");
            return false;
        }
        if (StringUtils.isNotEmpty(encode))
        {
            if (SMSConstants.ENCODE_GBK.equalsIgnoreCase(encode) 
                    && SMSConstants.ENCODE_UTF8.equalsIgnoreCase(encode))
            {
                LOGGER.warn("Encode " + encode + " is invalid");
                return false;
            }
        }
        
        return true;
    }
    
    private SMSMessage buildSMSMessage(String userName, String password,
        String encode, String to, String content)
    {
        SMSMessage smsMessage = new SMSMessage();

        //Set destination number
        String[] destMobileNumbers = to.split(",");       
        smsMessage.setDestId(destMobileNumbers);
        
        if (!StringUtils.isEmpty(content))
        {
            if (StringUtils.isEmpty(encode))
            {
                encode = SMSConstants.ENCODE_UTF8;
            }
            
            try
            {
                smsMessage.setContent(URLDecoder.decode(content, encode));
            }
            catch (UnsupportedEncodingException e)
            {
                smsMessage.setContent(content);
                LOGGER.error("", e);
            }
        }
        smsMessage.setEncode(SMSConstants.ENCODE_UTF8);
        smsMessage.setNeedReport(false);
        
        return smsMessage;
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
