package com.huawei.esdk.sms.device.sgip;

import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.platform.nemgr.itf.IDeviceConnection;
import com.huawei.esdk.sms.core.ErrorInfo;
import com.huawei.esdk.sms.device.SMSAdapterDevice;
import com.huawei.esdk.sms.exception.SDKSMSException;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.model.SMSubmitRes;

public class SMSAdapterDeviceSGIP extends SMSAdapterDevice
{
    private SGIPProxy12Gateway sgipProxyGateway;
    
    public SMSAdapterDeviceSGIP(String sap)
    {
        sgipProxyGateway = new SGIPProxy12Gateway(getInitParam(), this);
    }

    protected boolean isRightCfg(String prop)
    {
        return prop.startsWith("sgip");
    }
    
    @Override
    public ErrorInfo<String> sendSMSMessage(SMSMessage smsMessage)
        throws SDKSMSException
    {
        SMSubmitRes res = sgipProxyGateway.sendSms(smsMessage);
        ErrorInfo<String> result = processSendResult(res);
        PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + smsMessage.getSMS4Logging()
            + "] is delivered to SGIP Gateway successfully");
        return result;
    }

    @Override
    public IDeviceConnection createConnection(String connId, String sap, String loginUser, String loginPwd)
    {
        return new SGIPConnection(loginUser, loginPwd, this);
    }
}
