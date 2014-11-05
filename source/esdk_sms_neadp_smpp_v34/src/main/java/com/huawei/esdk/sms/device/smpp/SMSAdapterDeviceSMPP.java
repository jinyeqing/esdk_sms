package com.huawei.esdk.sms.device.smpp;


import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.platform.nemgr.itf.IDeviceConnection;
import com.huawei.esdk.sms.core.ErrorInfo;
import com.huawei.esdk.sms.device.SMSAdapterDevice;
import com.huawei.esdk.sms.exception.SDKSMSException;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.model.SMSubmitRes;

public class SMSAdapterDeviceSMPP extends SMSAdapterDevice
{
    private SMPPProxy34Gateway smppProxy34Gateway;
    
    public SMSAdapterDeviceSMPP(String sap)
    {
        smppProxy34Gateway = new SMPPProxy34Gateway(getInitParam(), this);
    }

    protected boolean isRightCfg(String prop)
    {
        return prop.startsWith("smpp");
    }
    
    @Override
    public ErrorInfo<String> sendSMSMessage(SMSMessage smsMessage)
        throws SDKSMSException
    {
        SMSubmitRes res = smppProxy34Gateway.sendSms(smsMessage);
        ErrorInfo<String> result = processSendResult(res);
        PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + smsMessage.getSMS4Logging()
            + "] is delivered to SMPP Gateway successfully");
        return result;
    }

    @Override
    public IDeviceConnection createConnection(String connId, String sap, String loginUser, String loginPwd)
    {
        return new SMPPConnection(loginUser, loginPwd, this);
    }
}
