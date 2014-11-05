package com.huawei.esdk.sms.openapi.soap.callback;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.cxf.common.util.StringUtils;
import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.constants.ESDKConstant;
import com.huawei.esdk.platform.common.utils.ApplicationContextUtil;
import com.huawei.esdk.platform.common.utils.Base64Utils;
import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.platform.commu.itf.ICXFSOAPProtocolAdapter;
import com.huawei.esdk.platform.commu.itf.IProtocolAdapterManager;
import com.huawei.esdk.platform.exception.ProtocolAdapterException;
import com.huawei.esdk.sms.core.IMsgCallback;
import com.huawei.esdk.sms.core.IMsgCallbackRegister;
import com.huawei.esdk.sms.model.SMSDeliveryStatus;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.model.SMSStatus;
import com.huawei.esdk.sms.openapi.soap.cxf.gen.client.ESGReceiveDeliveryInformation;
import com.huawei.esdk.sms.openapi.soap.cxf.gen.client.ESGReceiveDeliveryInformationList;
import com.huawei.esdk.sms.openapi.soap.cxf.gen.client.ESGReceiveISMStoESGMsg;
import com.huawei.esdk.sms.openapi.soap.cxf.gen.client.ESGReceiveISMStoESGMsgStatus;
import com.huawei.esdk.sms.openapi.soap.cxf.gen.client.ESGReceiveMessageFormat;
import com.huawei.esdk.sms.openapi.soap.cxf.gen.client.ESGReceiveNotifySmsDeliveryStatusRequest;
import com.huawei.esdk.sms.openapi.soap.cxf.gen.client.ESGReceiveNotifySmsReceptionRequest;
import com.huawei.esdk.sms.openapi.soap.cxf.gen.client.ESGReceiveSenderAddress;
import com.huawei.esdk.sms.openapi.soap.cxf.gen.client.ESGReceiveSmsServiceActivationNumber;

public class MsgCallbackSOAP implements IMsgCallback
{
    private static final Logger LOGGER = Logger.getLogger(MsgCallbackSOAP.class);
    
    private IProtocolAdapterManager protocolAdapterManager = ApplicationContextUtil.getBean("protocolAdapterManager");
    
    private ICXFSOAPProtocolAdapter cxfSOAPProtocolAdapter;
    
    private IMsgCallbackRegister msgCallbackRegister;
    
    public MsgCallbackSOAP()
    {
        String callbackServerURL = ConfigManager.getInstance().getValue("enterprise.side.callback.url");
        this.cxfSOAPProtocolAdapter =
            (ICXFSOAPProtocolAdapter)protocolAdapterManager.getProtocolInstanceByType(ESDKConstant.PROTOCOL_ADAPTER_TYPE_SOAP_CXF,
                callbackServerURL);
    }
    
    public void init()
    {
        msgCallbackRegister.registerMsgCallback(this);
    }
    
    @Override
    public void onSmsDeliveryStatus(SMSDeliveryStatus smsDeliveryStatus)
    {
        ESGReceiveNotifySmsDeliveryStatusRequest reqMessage;
        try
        {
            if (!"Y".equalsIgnoreCase(ConfigManager.getInstance().getValue("enterprise.side.report.service.on")))
            {
                return;
            }
            
            reqMessage = new ESGReceiveNotifySmsDeliveryStatusRequest();
            reqMessage.setRequestIdentifier(smsDeliveryStatus.getId());
            ESGReceiveDeliveryInformationList esgReceiveDeliveryInformationList = new ESGReceiveDeliveryInformationList();
            ESGReceiveDeliveryInformation item;
            for (SMSStatus smsStatus : smsDeliveryStatus.getStatusList())
            {
                item = new ESGReceiveDeliveryInformation();
                item.setAddress(smsStatus.getDestMobileNumber());
                item.setDeliveryStatus(getMappedStatus(smsStatus.getStatus()));
                esgReceiveDeliveryInformationList.getItem().add(item);
            }
            reqMessage.setDeliveryInformationList(esgReceiveDeliveryInformationList);
            
            cxfSOAPProtocolAdapter.syncSendMessageWithCxf(reqMessage,
                ESGReceiveISMStoESGMsgStatus.class.getName(),
                "notifySmsDeliveryStatusRequest");
        }
        catch (ProtocolAdapterException e)
        {
            LOGGER.error("Reception SMS delivered status send to Enterprise side failed", e);
        }
    }
    
    @Override
    public void onSmsReception(SMSMessage smsMessage)
    {
        ESGReceiveNotifySmsReceptionRequest reqMessage;
        try
        {
            reqMessage = new ESGReceiveNotifySmsReceptionRequest();
            ESGReceiveSenderAddress senderAddress = new ESGReceiveSenderAddress();
            ESGReceiveSmsServiceActivationNumber activationNumber = new ESGReceiveSmsServiceActivationNumber();
            reqMessage.setStrSenderAddress(senderAddress);
            reqMessage.setStrSmsServiceActivationNumber(activationNumber);
            
            senderAddress.getItem().add(smsMessage.getSrcId());
            activationNumber.getItem().addAll(Arrays.asList(smsMessage.getDestId()));
            reqMessage.setStrMessage(Base64Utils.encode(smsMessage.getContent().getBytes("UTF-8")));
            reqMessage.setEnMessageFormat(ESGReceiveMessageFormat.GB_2312);
            
            cxfSOAPProtocolAdapter.syncSendMessageWithCxf(reqMessage,
                ESGReceiveISMStoESGMsg.class.getName(),
                "notifySmsReception");
            
            PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + smsMessage.getSMS4Logging() + "] is delivered to enterprise system successfully");
        }
        catch (ProtocolAdapterException e)
        {
            LOGGER.error("Reception SMS delivered to Enterprise side failed", e);
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("UnsupportedEncodingException error", e);
        }
    }

    private String getMappedStatus(String status)
    {
        String result = null;
        if ("DELIVRD".equalsIgnoreCase(status) || "0".equalsIgnoreCase(status))
        {
            result = "Delivered";
        }
        else if("PENDING".equalsIgnoreCase(status) || "1".equalsIgnoreCase(status))
        {
            result = "MessageWaiting";
        }
        else if("FAILED".equalsIgnoreCase(status) || "2".equalsIgnoreCase(status))
        {
            result = "DeliveryImpossible";
        }
        else if ("UNKNOWN".equalsIgnoreCase(status))
        {
            result = "DeliveryUncertain";
        }
        else
        {
            if (isMASStatus(status))
            {
                result = status;
            }
        }
        
        if (StringUtils.isEmpty(status))
        {
            LOGGER.warn("The status " + status + " cannot be mapped to a proper MAS status");
            result = "DeliveryUncertain";
        }
        
        return result;
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
