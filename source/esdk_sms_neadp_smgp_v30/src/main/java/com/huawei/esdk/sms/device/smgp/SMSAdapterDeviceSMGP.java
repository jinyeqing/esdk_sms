package com.huawei.esdk.sms.device.smgp;

import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.platform.nemgr.itf.IDeviceConnection;
import com.huawei.esdk.sms.core.ErrorInfo;
import com.huawei.esdk.sms.device.SMSAdapterDevice;
import com.huawei.esdk.sms.exception.SDKSMSException;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.model.SMSubmitRes;

public class SMSAdapterDeviceSMGP extends SMSAdapterDevice
{
    private SMGPProxy30Gateway smgpProxy30Gateway;
    
    public SMSAdapterDeviceSMGP(String sap)
    {
        smgpProxy30Gateway = new SMGPProxy30Gateway(getInitParam(), this);
    }

    protected boolean isRightCfg(String prop)
    {
        return prop.startsWith("smgp");
    }
    
    @Override
    public ErrorInfo<String> sendSMSMessage(SMSMessage smsMessage)
        throws SDKSMSException
    {
        SMSubmitRes res = smgpProxy30Gateway.sendSms(smsMessage);
        ErrorInfo<String> result = processSendResult(res);
        PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + smsMessage.getSMS4Logging()
            + "] is delivered to SMGP Gateway successfully");
        return result;
    }

    @Override
    public IDeviceConnection createConnection(String connId, String sap, String loginUser, String loginPwd)
    {
        return new SMGPConnection(loginUser, loginPwd, this);
    }
}
