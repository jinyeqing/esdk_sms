package com.huawei.esdk.sms.device.mas20;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.constants.ESDKConstant;
import com.huawei.esdk.platform.common.utils.ApplicationContextUtil;
import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.platform.commu.itf.ICXFSOAPProtocolAdapter;
import com.huawei.esdk.platform.commu.itf.IProtocolAdapterManager;
import com.huawei.esdk.platform.exception.ProtocolAdapterException;
import com.huawei.esdk.platform.nemgr.itf.IDeviceConnection;
import com.huawei.esdk.sms.constants.SMSConstants;
import com.huawei.esdk.sms.core.ErrorInfo;
import com.huawei.esdk.sms.device.SMSAdapterDevice;
import com.huawei.esdk.sms.exception.SDKSMSException;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.north.royamas20.cxf.gen.client.CMAbility;
import com.huawei.esdk.sms.north.royamas20.cxf.gen.client.CmccMasWbs;
import com.huawei.esdk.sms.north.royamas20.cxf.gen.client.MessageFormat;
import com.huawei.esdk.sms.north.royamas20.cxf.gen.client.MessageNotificationType;
import com.huawei.esdk.sms.north.royamas20.cxf.gen.client.SendMethodType;
import com.huawei.esdk.sms.north.royamas20.cxf.gen.client.SendSmsRequest;
import com.huawei.esdk.sms.north.royamas20.cxf.gen.client.SendSmsResponse;
import com.huawei.esdk.sms.north.royamas20.cxf.gen.client.StartNotificationRequest;
import com.huawei.esdk.sms.north.royamas20.cxf.impl.CmccMasWbsImpl;
import com.huawei.esdk.sms.utils.DateUtils;
import com.huawei.esdk.sms.utils.SMSUtils;

public class SMSAdapterDeviceMAS20 extends SMSAdapterDevice
{
    private static final Logger LOGGER = Logger.getLogger(SMSAdapterDeviceMAS20.class);
    
    private IProtocolAdapterManager protocolAdapterManager = ApplicationContextUtil.getBean("protocolAdapterManager");
    
    private ICXFSOAPProtocolAdapter cxfSOAPProtocolAdapter;
    
    private String[] applicationIds;
    
    private CmccMasWbsImpl cmccMasWbs = ApplicationContextUtil.getBean("cmccMasWbs");
    
    public SMSAdapterDeviceMAS20(String sap)
    {
        String applicationIdConf = StringUtils.avoidNull(ConfigManager.getInstance().getValue("mas.application.id"));
        applicationIds = applicationIdConf.trim().split(",");
        
        String serverURL = sap;
        if (StringUtils.isEmpty(serverURL))
        {
            serverURL = ConfigManager.getInstance().getValue("mas.gateway.server.url");
        }
        
        prepareDeviceCapability(serverURL);
        this.cxfSOAPProtocolAdapter =
            (ICXFSOAPProtocolAdapter)protocolAdapterManager.getProtocolInstanceByType(ESDKConstant.PROTOCOL_ADAPTER_TYPE_SOAP_CXF,
                serverURL);
        
        cxfSOAPProtocolAdapter.setCXFSOAPCustProvider(new SMSCXFSOAPCustProvider());
        registerCallbackURL();
        cmccMasWbs.setSMSAdapterDeviceMAS20(this);
    }
    
    private void registerCallbackURL()
    {
        String localUrl = ConfigManager.getInstance().getValue("mas.local.service.url");
        if(StringUtils.isEmpty(localUrl))
        {
            return;
        }
        StartNotificationRequest startNotificationRequest = new StartNotificationRequest();
        
        MessageNotificationType messageNotificationType = new MessageNotificationType();
        messageNotificationType.setCMAbility(CMAbility.SMS_ABILITY);
        messageNotificationType.getWSURI().add(localUrl);
        startNotificationRequest.getMessageNotification().add(messageNotificationType);
        try
        {
            for (String applicationId : applicationIds)
            {
                startNotificationRequest.setApplicationId(applicationId);
                cxfSOAPProtocolAdapter.syncSendMessageWithCxf(startNotificationRequest,
                    CmccMasWbs.class.getName(),
                    "startNotification");
            }
        }
        catch (ProtocolAdapterException e)
        {
            LOGGER.error("Call MAS startNotification method failed", e);
        }
    }
    
    @Override
    public ErrorInfo<String> sendSMSMessage(SMSMessage smsMessage) throws SDKSMSException
    {
        ErrorInfo<String> result = new ErrorInfo<String>();
        try
        {
            SendSmsResponse sendSMSRes =
                (SendSmsResponse)cxfSOAPProtocolAdapter.syncSendMessageWithCxf(buildSendSmsRequest(smsMessage),
                    CmccMasWbs.class.getName(),
                    "sendSms");
            
            result.setValue(sendSMSRes.getRequestIdentifier());
            PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + smsMessage.getSMS4Logging()
                + "] is delivered to Roya MAS successfully");
        }
        catch (ProtocolAdapterException e)
        {
            PlatformSMSLogUtils.writeSendErrorLog("Fail to deliver A SMS  [" + smsMessage.getSMS4Logging()
                + "]  to Roya MAS");
            LOGGER.error("", e);
            smsMessage.setLastSendTime(DateUtils.getCurrentDateTime());
            SDKSMSException ex = new SDKSMSException("The SMS delveryed to MAS Gateway failed, eSDK will try to resend.");
            ex.setSdkErrCode(SMSConstants.SMS_SEND_ERROR);
            throw ex;
        }
        
        return result;
    }
    
    private SendSmsRequest buildSendSmsRequest(SMSMessage smsMessage)
    {
        SendSmsRequest reqMessage = new SendSmsRequest();
        reqMessage.setApplicationID(applicationIds[0]);
        reqMessage.setExtendCode(smsMessage.getSrcId());
        for (String destMobileNumber : smsMessage.getDestId())
        {
            if (!StringUtils.isEmpty(destMobileNumber))
            {
                reqMessage.getDestinationAddresses().add(SMSUtils.encodeNumberPrefix(SMSConstants.MAS_NUMBER_PREFIX,
                    destMobileNumber));
            }
        }
        
        reqMessage.setMessage(smsMessage.getContent());
        smsMessage.setEncode("GB2312");
        reqMessage.setMessageFormat(MessageFormat.fromValue(smsMessage.getEncode()));
        reqMessage.setSendMethod(SendMethodType.fromValue(decodeSendMethod(smsMessage.getContent())));
        reqMessage.setDeliveryResultRequest(smsMessage.isNeedReport());
        
        return reqMessage;
    }
    
    private String decodeSendMethod(String smsContent)
    {
        // 长消息，则发送type为Long
        int msgLength = smsContent.length();
        if (msgLength > 70)
        {
            return "Long";
        }
        
        return "Normal";
    }
    
    @Override
    public IDeviceConnection createConnection(String connId, String sap, String loginUser, String loginPwd)
    {
        return new MAS20Connection(loginUser, loginPwd, this);
    }
}
