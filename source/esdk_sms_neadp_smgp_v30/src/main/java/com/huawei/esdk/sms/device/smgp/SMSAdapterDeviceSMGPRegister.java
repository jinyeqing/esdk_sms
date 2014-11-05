package com.huawei.esdk.sms.device.smgp;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.sms.device.SMSAdapterDeviceBaseRegister;

public class SMSAdapterDeviceSMGPRegister extends SMSAdapterDeviceBaseRegister
{
    public void init()
    {
        if (StringUtils.isNotEmpty(ConfigManager.getInstance().getValue("smgp.host")))
        {
            super.init("SMSSMGP");
        }
    }
}