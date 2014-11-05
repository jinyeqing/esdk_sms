package com.huawei.esdk.sms.device.sgip;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.sms.device.SMSAdapterDeviceBaseRegister;

public class SMSAdapterDeviceSGIPRegister extends SMSAdapterDeviceBaseRegister
{
    public void init()
    {
        if (StringUtils.isNotEmpty(ConfigManager.getInstance().getValue("sgip.host")))
        {
            super.init("SMSSGIP");
        }
    }
}