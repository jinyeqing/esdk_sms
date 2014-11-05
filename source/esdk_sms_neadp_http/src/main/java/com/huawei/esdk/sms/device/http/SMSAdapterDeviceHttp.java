package com.huawei.esdk.sms.device.http;

import java.util.Map;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.ThreadLocalHolder;
import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.constants.ESDKConstant;
import com.huawei.esdk.platform.common.utils.ApplicationContextUtil;
import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.platform.commu.itf.IProtocolAdapterManager;
import com.huawei.esdk.platform.commu.itf.ISDKProtocolAdapter;
import com.huawei.esdk.platform.exception.ProtocolAdapterException;
import com.huawei.esdk.platform.nemgr.itf.IDeviceConnection;
import com.huawei.esdk.sms.constants.SMSConstants;
import com.huawei.esdk.sms.core.ErrorInfo;
import com.huawei.esdk.sms.device.SMSAdapterDevice;
import com.huawei.esdk.sms.exception.SDKSMSException;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.north.http.commu.SMSHttpProtocolAdatperCustProvider;
import com.huawei.esdk.sms.north.http.notify.INotifyCollector;
import com.huawei.esdk.sms.utils.DateUtils;

public class SMSAdapterDeviceHttp extends SMSAdapterDevice
{
    private static final Logger LOGGER = Logger.getLogger(SMSAdapterDeviceHttp.class);
    
    private IProtocolAdapterManager protocolAdapterManager = ApplicationContextUtil.getBean("protocolAdapterManager");
    
    private ISDKProtocolAdapter sdkProtocolAdapter;
    
    private String successResCode;
    
    private INotifyCollector notifyCollector = ApplicationContextUtil.getBean("smsNotifyCollector");
    
    public SMSAdapterDeviceHttp(String sap)
    {
        String serverURL = sap;
        if (StringUtils.isEmpty(serverURL))
        {
            serverURL = ConfigManager.getInstance().getValue("sms.http.gateway.url");
        }
        
        successResCode = ConfigManager.getInstance().getValue("sms.http.gateway.success.response.code");
        
        prepareDeviceCapability(serverURL);
        
        if ("HTTP_JDK".equalsIgnoreCase(ConfigManager.getInstance().getValue("sms.http.impl.way")))
        {
            this.sdkProtocolAdapter =
                protocolAdapterManager.getProtocolInstanceByType(ESDKConstant.PROTOCOL_ADAPTER_TYPE_HTTP_JDK, serverURL);
        }
        else
        {
            this.sdkProtocolAdapter =
                protocolAdapterManager.getProtocolInstanceByType(ESDKConstant.PROTOCOL_ADAPTER_TYPE_HTTP, serverURL);
        }
        sdkProtocolAdapter.setSdkProtocolAdatperCustProvider(new SMSHttpProtocolAdatperCustProvider());
        
        notifyCollector.setSMSAdapterDevice(this);
    }
    
    @Override
    public ErrorInfo<String> sendSMSMessage(SMSMessage smsMessage)
        throws SDKSMSException
    {
        ErrorInfo<String> result = new ErrorInfo<String>();
        try
        {
            @SuppressWarnings("unchecked")
            Map<String, String> resultMap =
                (Map<String, String>)sdkProtocolAdapter.syncSendMessage(smsMessage,
                	ConfigManager.getInstance().getValue("sms.http.gateway.xml.request.method"),
                    "java.util.Map");
            result.setValue(resultMap.get("@{serialNo}"));
            String resCode = resultMap.get("@{ResponseCode}");
            //For the resCode format: result Code,result description
            if (null != resCode && resCode.contains(","))
            {
                resCode = resCode.split(",")[0];
            }
            if (requestSendSuccessful(resCode))
            {
                if (StringUtils.isEmpty(resCode))
                {
                    resCode = "0";
                }
                PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + smsMessage.getSMS4Logging()
                    + "] is delivered to HTTP Gateway successfully");
            }
            else
            {
                LOGGER.info("Response Code = " + resCode);
                String errorMsg = resultMap.get("@{ErrorMessage}");
                StringBuilder sb =
                    new StringBuilder("A SMS [" + smsMessage.getSMS4Logging() + "] is delivered to HTTP Gateway");
                sb.append(" but is not processed successfully by Gateway, the response code=");
                sb.append(resCode);
                sb.append(" and the error message is " + errorMsg);
                PlatformSMSLogUtils.writeSendErrorLog(sb.toString());
            }
            
            ThreadLocalHolder.get().getEntities().put("resultCode", resCode);
        }
        catch (ProtocolAdapterException e)
        {
            LOGGER.error("A SMS [" + smsMessage.getSMS4Logging() + "] is delivered to HTTP Gateway failed", e);
            smsMessage.setLastSendTime(DateUtils.getCurrentDateTime());
            SDKSMSException ex =
                new SDKSMSException("The SMS delveryed to HTTP Gateway failed, eSDK will try to resend.");
            ex.setSdkErrCode(SMSConstants.SMS_SEND_ERROR);
            throw ex;
        }
        
        return result;
    }
    
    protected boolean requestSendSuccessful(String resCode)
    {
        if (!StringUtils.isEmpty(successResCode))
        {
            if (!successResCode.equals(resCode))
            {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public IDeviceConnection createConnection(String connId, String sap, String loginUser, String loginPwd)
    {
        return new HttpConnection(loginUser, loginPwd, this);
    }
}
