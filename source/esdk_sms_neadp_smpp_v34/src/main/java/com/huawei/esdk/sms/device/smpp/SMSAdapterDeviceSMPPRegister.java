package com.huawei.esdk.sms.device.smpp;


import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.sms.device.SMSAdapterDeviceBaseRegister;

public class SMSAdapterDeviceSMPPRegister extends SMSAdapterDeviceBaseRegister
{
    public void init()
    {
        if (StringUtils.isNotEmpty(ConfigManager.getInstance().getValue("smpp.host")))
        {
            super.init("SMSSMPP");
        }
    }
}