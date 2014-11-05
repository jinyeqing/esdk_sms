package com.huawei.esdk.sms.north.http.notify;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.sms.device.SMSAdapterDevice;

public class NotifyCollector implements INotifyCollector
{
    private static Logger LOGGER = Logger.getLogger(NotifyCollector.class);
    
    private SMSAdapterDevice smsAdapterDevice;
    
    private String getSMSSOAPAction;
    
    private String getDeliveryStatusSOAPAction;
    
    public void init()
    {
        getSMSSOAPAction = ConfigManager.getInstance().getValue("sms.http.gateway.get.sms.soapaction");
        getDeliveryStatusSOAPAction = ConfigManager.getInstance().getValue("sms.http.gateway.get.delivery.status.soapaction");
        
        if (StringUtils.isNotEmpty(getSMSSOAPAction))
        {
            (new Thread(new GetSMSTask(smsAdapterDevice))).start();
            LOGGER.info("Get SMS Task started");
        }
        
        if (StringUtils.isNotEmpty(getDeliveryStatusSOAPAction))
        {
            (new Thread(new GetDeliveryStatusTask(smsAdapterDevice))).start();
            LOGGER.info("Get Delivery status Task started");
        }
    }
    
    public void setSMSAdapterDevice(SMSAdapterDevice smsAdapterDevice)
    {
        this.smsAdapterDevice = smsAdapterDevice;
        init();
    }
}
