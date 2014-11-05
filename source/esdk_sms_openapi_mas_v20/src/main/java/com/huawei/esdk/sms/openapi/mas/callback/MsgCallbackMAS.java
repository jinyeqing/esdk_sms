package com.huawei.esdk.sms.openapi.mas.callback;


import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.constants.ESDKConstant;
import com.huawei.esdk.platform.common.utils.ApplicationContextUtil;
import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.platform.commu.itf.ICXFSOAPProtocolAdapter;
import com.huawei.esdk.platform.commu.itf.IProtocolAdapterManager;
import com.huawei.esdk.platform.exception.ProtocolAdapterException;
import com.huawei.esdk.sms.core.IMsgCallback;
import com.huawei.esdk.sms.core.IMsgCallbackRegister;
import com.huawei.esdk.sms.model.SMSDeliveryStatus;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.model.SMSStatus;
import com.huawei.esdk.sms.openapi.mas.cxf.gen.client.DeliveryInformation;
import com.huawei.esdk.sms.openapi.mas.cxf.gen.client.DeliveryStatus;
import com.huawei.esdk.sms.openapi.mas.cxf.gen.client.MasWbsClient;
import com.huawei.esdk.sms.openapi.mas.cxf.gen.client.MessageFormat;
import com.huawei.esdk.sms.openapi.mas.cxf.gen.client.NotifySmsDeliveryStatusRequest;
import com.huawei.esdk.sms.openapi.mas.cxf.gen.client.NotifySmsReceptionRequest;

public class MsgCallbackMAS implements IMsgCallback
{
    private static final Logger LOGGER = Logger.getLogger(MsgCallbackMAS.class);
    
    private IProtocolAdapterManager protocolAdapterManager = ApplicationContextUtil.getBean("protocolAdapterManager");
    
    private ICXFSOAPProtocolAdapter cxfSOAPProtocolAdapter;
    
    private IMsgCallbackRegister msgCallbackRegister;
    
    public MsgCallbackMAS()
    {
    }
    
    public void registerCallback(String callbackServerURL)
    {
        this.cxfSOAPProtocolAdapter =
            (ICXFSOAPProtocolAdapter)protocolAdapterManager.getProtocolInstanceByType(ESDKConstant.PROTOCOL_ADAPTER_TYPE_SOAP_CXF,
                callbackServerURL);
    }
    
    public void unRegisterCallback()
    {
        this.cxfSOAPProtocolAdapter = null;
    }
    
    public void init()
    {
        msgCallbackRegister.registerMsgCallback(this);
    }
    
    @Override
    public void onSmsDeliveryStatus(SMSDeliveryStatus smsDeliveryStatus)
    {
        if (null == cxfSOAPProtocolAdapter)
        {
            return;
        }
        
        NotifySmsDeliveryStatusRequest reqMessage;
        try
        {
            if (!"Y".equalsIgnoreCase(ConfigManager.getInstance().getValue("enterprise.side.report.service.on")))
            {
                return;
            }
            
            reqMessage = new NotifySmsDeliveryStatusRequest();
            reqMessage.setRequestIdentifier(smsDeliveryStatus.getId());
            DeliveryInformation item;
            for (SMSStatus smsStatus : smsDeliveryStatus.getStatusList())
            {
                item = new DeliveryInformation();
                item.setAddress(smsStatus.getDestMobileNumber());
                item.setDeliveryStatus(getMappedStatus(smsStatus.getStatus()));
                reqMessage.getDeliveryInformation().add(item);
            }
            
            cxfSOAPProtocolAdapter.syncSendMessageWithCxf(reqMessage,
                MasWbsClient.class.getName(),
                "notifySmsDeliveryStatus");
        }
        catch (ProtocolAdapterException e)
        {
            LOGGER.error("Reception SMS delivered status send to Enterprise side failed", e);
        }
    }
    
    @Override
    public void onSmsReception(SMSMessage smsMessage)
    {
        if (null == cxfSOAPProtocolAdapter)
        {
            return;
        }
        
        NotifySmsReceptionRequest reqMessage;
        try
        {
            reqMessage = new NotifySmsReceptionRequest();
            com.huawei.esdk.sms.openapi.mas.cxf.gen.client.SMSMessage message = new com.huawei.esdk.sms.openapi.mas.cxf.gen.client.SMSMessage();
            reqMessage.setMessage(message);
            
            message.setSenderAddress(smsMessage.getSrcId());
            message.setSmsServiceActivationNumber(smsMessage.getDestIdAsString());
            
//            message.setMessage(Base64Utils.encode(smsMessage.getContent().getBytes("UTF-8")));
            message.setMessage(smsMessage.getContent());
            message.setMessageFormat(MessageFormat.GB_2312);
            
            cxfSOAPProtocolAdapter.syncSendMessageWithCxf(reqMessage,
                MasWbsClient.class.getName(),
                "notifySmsReception");
            
            PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + smsMessage.getSMS4Logging() + "] is delivered to enterprise system successfully");
        }
        catch (ProtocolAdapterException e)
        {
            LOGGER.error("Reception SMS delivered to Enterprise side failed", e);
        }
//        catch (UnsupportedEncodingException e)
//        {
//            LOGGER.error("UnsupportedEncodingException error", e);
//        }
    }

    private DeliveryStatus getMappedStatus(String status)
    {
        DeliveryStatus deliveryStatus = null;
        if ("DELIVRD".equalsIgnoreCase(status) || "0".equalsIgnoreCase(status))
        {
            deliveryStatus = DeliveryStatus.DELIVERED;
        }
        else if("PENDING".equalsIgnoreCase(status) || "1".equalsIgnoreCase(status))
        {
            deliveryStatus = DeliveryStatus.MESSAGE_WAITING;
        }
        else if("FAILED".equalsIgnoreCase(status) || "2".equalsIgnoreCase(status))
        {
            deliveryStatus = DeliveryStatus.DELIVERY_IMPOSSIBLE;
        }
        else if ("UNKNOWN".equalsIgnoreCase(status))
        {
            deliveryStatus = DeliveryStatus.DELIVERY_UNCERTAIN;
        }
        else
        {
            if (isMASStatus(status))
            {
                deliveryStatus = DeliveryStatus.fromValue(status);
            }
        }
        
        if (null == deliveryStatus)
        {
            LOGGER.warn("The status " + status + " cannot be mapped to a proper MAS status");
            deliveryStatus = DeliveryStatus.DELIVERY_UNCERTAIN;
        }
        
        return deliveryStatus;
    }
    
    private boolean isMASStatus(String status)
    {
        if (null == status)
        {
            return false;
        }
            
        if (status.startsWith("Deliver") || status.equalsIgnoreCase("MessageWaiting") || status.equalsIgnoreCase("KeyWordFilterFailed"))
        {
            return true;
        }
        
        return false;
    }

    public IMsgCallbackRegister getMsgCallbackRegister()
    {
        return msgCallbackRegister;
    }

    public void setMsgCallbackRegister(IMsgCallbackRegister msgCallbackRegister)
    {
        this.msgCallbackRegister = msgCallbackRegister;
    }
}
