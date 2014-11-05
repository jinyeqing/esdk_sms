package com.huawei.esdk.sms.device.cmpp;


import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.sms.device.SMSAdapterDeviceBaseRegister;

public class SMSAdapterDeviceCMPPRegister extends SMSAdapterDeviceBaseRegister
{
    public void init()
    {
        if (StringUtils.isNotEmpty(ConfigManager.getInstance().getValue("cmpp.host")))
        {
            super.init("SMSCMPP");
        }
    }
}