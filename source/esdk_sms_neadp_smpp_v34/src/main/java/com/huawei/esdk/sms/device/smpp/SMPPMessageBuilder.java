package com.huawei.esdk.sms.device.smpp;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.NumberUtils;
import com.huawei.esdk.sms.constants.SMSConstants;
import com.huawei.esdk.sms.device.BaseMessageBuilder;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.smproxy.comm.smpp.message.DestAddresses;
import com.huawei.smproxy.comm.smpp.message.SMPPSubmitMessage;
import com.huawei.smproxy.comm.smpp.message.SMPPSubmitMuitlMessage;

public class SMPPMessageBuilder extends BaseMessageBuilder
{
    private static int addrTon;
    
    private static int addrNpi;
    
    private static int esmClass;
    
    private static String serivceType;
    
    private static int protocolId;
    
    private static int priorityFlag;
    
    private static int replaceIfPresentFlag;
    
    private static int smDefaultMsgId;
    
    static
    {
        addrTon = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("smpp.addr-ton", "5"));
        addrNpi = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("smpp.addr-npi", "0"));
        esmClass = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("smpp.esm-class", "0"));
        serivceType = ConfigManager.getInstance().getValue("smpp.service-type");
        protocolId = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("smpp.protocol-id", "0"));
        priorityFlag = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("smpp.priority-flag", "0"));
        replaceIfPresentFlag = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("smpp.replace-if-present-flag", "0"));
        smDefaultMsgId = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("smpp.sm-default-msg-id", "0"));
    }
    
    public List<SMPPSubmitMessage> buildSMPPSubmitMessages(SMSMessage message, int normalSMMaxLength, int dataCoding)
        throws IllegalArgumentException, UnsupportedEncodingException
    {
        List<SMPPSubmitMessage> result = new ArrayList<SMPPSubmitMessage>();
        int registeredDelivery = message.isNeedReport() ? 1 : 0;
        int smsLength = message.getContent().getBytes(getCharset(dataCoding)).length;
        int localEsmClass = esmClass;
        
        if (smsLength > normalSMMaxLength)
        {
            //长短信
            localEsmClass = ESM_CLASS_UDHI;
            dataCoding = DATA_ENCODING_UCS2;
            
            List<byte[]> smsList =
                splitLongMessage(message.getContent(), getCharset(dataCoding), normalSMMaxLength, 6);
            for (byte[] item : smsList)
            {
                result.add(doBuildSMPPSubmitMessage(message.getSrcId(),
                    message.getDestIdAsString(),
                    localEsmClass,
                    registeredDelivery,
                    dataCoding,
                    item));
            }
        }
        else
        {
            //普通短信
            result.add(doBuildSMPPSubmitMessage(message.getSrcId(),
                message.getDestIdAsString(),
                localEsmClass,
                registeredDelivery,
                dataCoding,
                message.getContent().getBytes(getCharset(dataCoding))));
        }
        
        return result;
    }
    
    public SMPPSubmitMessage doBuildSMPPSubmitMessage(String srcId, String destId, int esmClass,
        int registeredDelivery, int dataCoding, byte[] content)
    {
        return new SMPPSubmitMessage(serivceType,//String serviceType
            (byte)addrTon,// byte sourceAddrTon
            (byte)addrNpi,// byte sourceAddrNpi, 
            encodeNumber(srcId),// String sourceAddr
            (byte)addrTon,//byte destAddrTon
            (byte)addrNpi,//byte destAddrNpi
            destId,// String destinationAddr
            (byte)esmClass,//byte esmClass,
            (byte)protocolId,// byte protocolId
            (byte)priorityFlag,// byte priorityFlag
            "",// String scheduleDeliveryTime
            "",// String validityPeriod
            (byte)registeredDelivery,// byte registeredDelivery//状态报告
            (byte)replaceIfPresentFlag,// byte replaceIfPresentFlag
            (byte)dataCoding,// byte dataCoding
            (byte)smDefaultMsgId,//byte smDefaultMsgId
            content.length,//int smLength
            content//byte shortMessage[]
        );
    }
    
    public List<SMPPSubmitMuitlMessage> buildSMPPSubmitMuitlMessages(SMSMessage message, int normalSMSLength,
        int dataCoding)
        throws UnsupportedEncodingException
    {
        List<SMPPSubmitMuitlMessage> result = new ArrayList<SMPPSubmitMuitlMessage>();
        int registeredDelivery = message.isNeedReport() ? 1 : 0;
        
        int smsLength = message.getContent().getBytes(getCharset(dataCoding)).length;
        int localEsmClass = esmClass;
        
        if (smsLength > normalSMSLength)
        {
            //长短信
            localEsmClass = ESM_CLASS_UDHI;
            dataCoding = DATA_ENCODING_UCS2;
            List<byte[]> smsList =
                splitLongMessage(message.getContent(), getCharset(dataCoding), normalSMSLength, 6);
            for (byte[] item : smsList)
            {
                result.add(doBuildSMPPSubmitMuitlMessage(registeredDelivery, localEsmClass, message, dataCoding, item));
            }
        }
        else
        {
            result.add(doBuildSMPPSubmitMuitlMessage(registeredDelivery,
                localEsmClass,
                message,
                dataCoding,
                message.getContent().getBytes(getCharset(dataCoding))));
        }
        
        return result;
    }
    
    private SMPPSubmitMuitlMessage doBuildSMPPSubmitMuitlMessage(int registeredDelivery, int esmClass,
        SMSMessage message, int dataCoding, byte[] sendContent)
        throws UnsupportedEncodingException
    {
        DestAddresses[] destAddresses = new DestAddresses[message.getDestId().length];
        DestAddresses item;
        for (int i = 0; i < message.getDestId().length; i++)
        {
            item = new DestAddresses();
            /*
             * 1 - SME Address
             * 2 - Distribution List Name
             */
            item.setDesFlag(1);
            item.setDestAddrNpi(addrNpi);
            item.setDestAddrTon(addrTon);
            item.setDestinationAddr(message.getDestId()[i]);
            destAddresses[i] = item;
        }
        return new SMPPSubmitMuitlMessage(serivceType,//String serviceType
            (byte)addrTon,// byte sourceAddrTon
            (byte)addrNpi,// byte sourceAddrNpi,
            encodeNumber(message.getSrcId()),// String sourceAddr
            (byte)message.getDestId().length,//byte numberOfDests, 
            destAddresses,// DestAddresses destAddresses[],
            (byte)esmClass,//byte esmClass,
            (byte)protocolId,// byte protocolId
            (byte)priorityFlag,// byte priorityFlag
            "",// String scheduleDeliveryTime
            "",// String validityPeriod
            (byte)registeredDelivery,// byte registeredDelivery//状态报告
            (byte)replaceIfPresentFlag,// byte replaceIfPresentFlag
            (byte)dataCoding,// byte dataCoding
            (byte)smDefaultMsgId,//byte smDefaultMsgId
            sendContent.length,//int smLength
            sendContent//byte shortMessage[]
        );
    }

    @Override
    protected String getMappingProp()
    {
        return "smpp.data-coding-charset-mapping";
    }

    @Override
    protected String getSpNumber()
    {
        return SMSConstants.SP_NUMBER;
    }
    
    protected String getProtocolType()
    {
        return "smpp";
    }
}
