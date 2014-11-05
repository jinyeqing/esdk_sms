package com.huawei.esdk.sms.device;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.bean.config.DeviceConfig;
import com.huawei.esdk.platform.common.exception.SDKException;
import com.huawei.esdk.platform.common.utils.ApplicationContextUtil;
import com.huawei.esdk.platform.nemgr.itf.IDeviceManager;
import com.huawei.esdk.sms.core.IRecvSMSCallback;
import com.huawei.esdk.sms.core.ISMSAdapter;
import com.huawei.esdk.sms.core.impl.SMSAdapterRegister;

public abstract class SMSAdapterDeviceBaseRegister
{
    private static final Logger LOGGER = Logger.getLogger(SMSAdapterDeviceBaseRegister.class);
    
    protected IDeviceManager deviceManager = ApplicationContextUtil.getBean("deviceManager");
    
    protected SMSAdapterRegister smsAdapterRegister;
    
    protected void init(String deviceIdKey)
    {
        ISMSAdapter smsAdapter = null;
        try
        {
            List<DeviceConfig> devices = getDevices(deviceIdKey);
            String deviceId;
            for (DeviceConfig item : devices)
            {
                deviceId = item.getDeviceId();
                smsAdapter = deviceManager.getDeviceServiceProxy(deviceId, ISMSAdapter.class);
                smsAdapter.setSMSAdapterId(deviceId);
                
                smsAdapterRegister.registerMsgCallback(smsAdapter);
                
                IRecvSMSCallback callback = (IRecvSMSCallback)ApplicationContextUtil.getBean("dispatcher");
                smsAdapter.registerRecvCallback(callback);
            }
        }
        catch (SDKException e)
        {
            LOGGER.error("", e);
        }
    }
    
    protected List<DeviceConfig> getDevices(String key)
    {
        List<DeviceConfig> result = new ArrayList<DeviceConfig>();
        List<DeviceConfig> devices = deviceManager.queryAllDeviceInfo();
        
        for (DeviceConfig item : devices)
        {
            if (item.getDeviceType().toUpperCase(Locale.getDefault()).contains(key))
            {
                result.add(item);
            }
        }
        
        return result;
    }
    
    public SMSAdapterRegister getSmsAdapterRegister()
    {
        return smsAdapterRegister;
    }
    
    public void setSmsAdapterRegister(SMSAdapterRegister smsAdapterRegister)
    {
        this.smsAdapterRegister = smsAdapterRegister;
    }
}
