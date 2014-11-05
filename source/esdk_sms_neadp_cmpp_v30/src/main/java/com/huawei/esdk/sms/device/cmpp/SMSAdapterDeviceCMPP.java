package com.huawei.esdk.sms.device.cmpp;

import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.platform.nemgr.itf.IDeviceConnection;
import com.huawei.esdk.sms.core.ErrorInfo;
import com.huawei.esdk.sms.device.SMSAdapterDevice;
import com.huawei.esdk.sms.exception.SDKSMSException;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.model.SMSubmitRes;

public class SMSAdapterDeviceCMPP extends SMSAdapterDevice
{
    private CMPPProxy30Gateway cmppProxy30Gateway;
    
    public SMSAdapterDeviceCMPP(String sap)
    {
        cmppProxy30Gateway = new CMPPProxy30Gateway(getInitParam(), this);
    }

    protected boolean isRightCfg(String prop)
    {
        return prop.startsWith("cmpp");
    }
    
    @Override
    public ErrorInfo<String> sendSMSMessage(SMSMessage smsMessage)
        throws SDKSMSException
    {
        SMSubmitRes res = cmppProxy30Gateway.sendSms(smsMessage);
        ErrorInfo<String> result = processSendResult(res);
        PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + smsMessage.getSMS4Logging()
            + "] is delivered to CMPP Gateway successfully");
        return result;
    }

    @Override
    public IDeviceConnection createConnection(String connId, String sap, String loginUser, String loginPwd)
    {
        return new CMPPConnection(loginUser, loginPwd, this);
    }
}
