package com.huawei.esdk.sms.north.royamas20.cxf.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.sms.constants.SMSConstants;
import com.huawei.esdk.sms.device.mas20.SMSAdapterDeviceMAS20;
import com.huawei.esdk.sms.model.SMSDeliveryStatus;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.model.SMSStatus;
import com.huawei.esdk.sms.north.royamas20.cxf.gen.server.CmccMasWbs;
import com.huawei.esdk.sms.north.royamas20.cxf.gen.server.DeliveryInformation;
import com.huawei.esdk.sms.north.royamas20.cxf.gen.server.NotifySmsDeliveryStatusRequest;
import com.huawei.esdk.sms.north.royamas20.cxf.gen.server.NotifySmsReceptionRequest;
import com.huawei.esdk.sms.north.royamas20.cxf.gen.server.PolicyException_Exception;
import com.huawei.esdk.sms.north.royamas20.cxf.gen.server.ServiceException_Exception;
import com.huawei.esdk.sms.utils.SMSUtils;

public class CmccMasWbsImpl implements CmccMasWbs
{
    private static final Logger LOGGER = Logger.getLogger(CmccMasWbsImpl.class);
    
    private SMSAdapterDeviceMAS20 smsAdapterDeviceMAS20;
    
    private String chinaMobileSPCode;
    
    private String chinaUnicomSPCode;
    
    private String chinaTelecomSPCode;
    
    public CmccMasWbsImpl()
    {
        chinaMobileSPCode = ConfigManager.getInstance().getValue("sp.number.chinamobile");
        chinaUnicomSPCode = ConfigManager.getInstance().getValue("sp.number.chinaunicom");
        chinaTelecomSPCode = ConfigManager.getInstance().getValue("sp.number.chinatelecom");
    }
    
    @Override
    public void notifySmsReception(NotifySmsReceptionRequest notifySmsReceptionRequest)
        throws ServiceException_Exception, PolicyException_Exception
    {
        LOGGER.debug("enter notifySmsReception is called");
        SMSMessage smsMessage = new SMSMessage();
        smsMessage.setSrcId(getPurePhoneNumber(notifySmsReceptionRequest.getMessage().getSenderAddress()));
        smsMessage.setDestId(new String[] {getPurePhoneNumber(notifySmsReceptionRequest.getMessage()
            .getSmsServiceActivationNumber())});
        smsMessage.setContent(notifySmsReceptionRequest.getMessage().getMessage());
        smsMessage.setEncode(notifySmsReceptionRequest.getMessage().getMessageFormat().value());
        PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + smsMessage.getSMS4Logging() + "] arrived  successfully from Roya MAS");
        smsAdapterDeviceMAS20.onRecvIncomeSMS(smsMessage);
    }
    
    @Override
    public void notifySmsDeliveryStatus(NotifySmsDeliveryStatusRequest notifySmsDeliveryStatusRequest)
        throws ServiceException_Exception, PolicyException_Exception
    {
        LOGGER.debug("enter notifySmsDeliveryStatus");
        SMSDeliveryStatus smsDeliveryStatus = new SMSDeliveryStatus();
        smsDeliveryStatus.setId(notifySmsDeliveryStatusRequest.getRequestIdentifier());
        
        List<SMSStatus> statusList = new ArrayList<SMSStatus>(4);
        SMSStatus smsStatus;
        for (DeliveryInformation deliveryInformation : notifySmsDeliveryStatusRequest.getDeliveryInformation())
        {
            smsStatus = new SMSStatus();
            smsStatus.setDestMobileNumber(getPurePhoneNumber(deliveryInformation.getAddress()));
            
            smsStatus.setStatus(deliveryInformation.getDeliveryStatus().value());
            statusList.add(smsStatus);
        }
        smsDeliveryStatus.setStatusList(statusList);
        
        smsAdapterDeviceMAS20.onRecvReceipt(smsDeliveryStatus);
    }
    
    public void setSMSAdapterDeviceMAS20(SMSAdapterDeviceMAS20 smsAdapterDeviceMAS20)
    {
        this.smsAdapterDeviceMAS20 = smsAdapterDeviceMAS20;
    }
    
    private String getPurePhoneNumber(String number)
    {
        if (null == number)
        {
            return number;
        }
        
        number = SMSUtils.decodeNumberPrefix(SMSConstants.MAS_NUMBER_PREFIX, number);
        String spCode = null;
        if (StringUtils.isNotEmpty(chinaMobileSPCode) && number.startsWith(chinaMobileSPCode))
        {
            spCode = chinaMobileSPCode;
        }
        else if (StringUtils.isNotEmpty(chinaUnicomSPCode) && number.startsWith(chinaUnicomSPCode))
        {
            spCode = chinaUnicomSPCode;
        }
        else if (StringUtils.isNotEmpty(chinaTelecomSPCode) && number.startsWith(chinaTelecomSPCode))
        {
            spCode = chinaTelecomSPCode;
        }
        
        return SMSUtils.decodeSPNumber(spCode, number);
    }
}
