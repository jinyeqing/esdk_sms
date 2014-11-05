package com.huawei.esdk.sms.openapi.smpp.message;

import java.io.UnsupportedEncodingException;

import com.huawei.esdk.platform.common.utils.BytesUtils;

public class SMPPDeliverMessage extends SMPPPDU
{
    private String serviceType;
    
    private int sourceAddrTon;
    
    private int sourceAddrNpi;
    
    private String sourceAddr;
    
    private int destAddrTon;
    
    private int destAddrNpi;
    
    private String destinationAddr;
    
    private int esmClass;
    
    private int protocolId;
    
    private int priorityFlag;
    
    private String scheduleDeliveryTime;
    
    private String validityPeriod;
    
    private int registeredDelivery;
    
    private int replaceIfPresentFlag;
    
    private int dataCoding;
    
    private int smDefaultMsgId;
    
    private int smLength;
    
    private String shortMessage;
    
    public SMPPDeliverMessage()
    {
        
    }
    
    public SMPPDeliverMessage(String serviceType, int sourceAddrTon, int sourceAddrNpi, String sourceAddr,
        int destAddrTon, int destAddrNpi, String destinationAddr, int esmClass, int protocolId, int priorityFlag,
        int registeredDelivery, int dataCoding, int smLength, String shortMessage)
    {
        this.serviceType = serviceType;
        this.sourceAddrTon = sourceAddrTon;
        this.sourceAddrNpi = sourceAddrTon;
        this.sourceAddr = sourceAddr;
        this.destAddrTon = destAddrTon;
        this.destAddrNpi = destAddrNpi;
        this.destinationAddr = destinationAddr;
        this.esmClass = esmClass;
        this.protocolId = protocolId;
        this.priorityFlag = priorityFlag;
        this.registeredDelivery = registeredDelivery;
        this.dataCoding = dataCoding;
        this.smLength = smLength;
        this.shortMessage = shortMessage;
    }
    
    public void pack()
    {
        byte[] shortMessageByte;
        try
        {
            shortMessageByte = shortMessage.getBytes(getCharset(dataCoding));
        }
        catch (UnsupportedEncodingException e)
        {
            shortMessageByte = BytesUtils.getBytes(shortMessage);
        }
        
        int len =
            16 + BytesUtils.getBytes(serviceType).length + 1 + 1 + 1 + BytesUtils.getBytes(sourceAddr).length + 1 + 1 + 1
                + BytesUtils.getBytes(destinationAddr).length + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1
                + shortMessageByte.length;
        setCommandLength(len);
        setCommandStatus(0);
        //TODO unique number
        setSequenceNumber(0);
        
        byte[] buf = new byte[len];
        super.pack(buf);
        
        int pos = SMPP_HEADER_LENGTH;
        byte tempbuf[] = BytesUtils.getBytes(this.serviceType);
        System.arraycopy(tempbuf, 0, buf, pos, tempbuf.length);
        pos += tempbuf.length + 1;
        buf[pos++] = (byte)sourceAddrTon;
        buf[pos++] = (byte)sourceAddrNpi;
        tempbuf = BytesUtils.getBytes(this.sourceAddr);
        System.arraycopy(tempbuf, 0, buf, pos, tempbuf.length);
        pos += tempbuf.length + 1;
        buf[pos++] = (byte)destAddrTon;
        buf[pos++] = (byte)destAddrNpi;
        tempbuf = BytesUtils.getBytes(this.destinationAddr);
        System.arraycopy(tempbuf, 0, buf, pos, tempbuf.length);
        pos += tempbuf.length + 1;
        buf[pos++] = (byte)esmClass;
        buf[pos++] = (byte)protocolId;
        buf[pos++] = (byte)priorityFlag;
        buf[pos++] = 0;
        buf[pos++] = 0;
        buf[pos++] = (byte)registeredDelivery;
        buf[pos++] = 0;
        buf[pos++] = (byte)dataCoding;
        buf[pos++] = 0;
        buf[pos++] = (byte)smLength;
        tempbuf = shortMessageByte;
        System.arraycopy(tempbuf, 0, buf, pos, tempbuf.length);
        
        setByteData(buf);
    }
    
    public void unpack()
    {
        
    }
    
    public String getServiceType()
    {
        return serviceType;
    }
    
    public void setServiceType(String serviceType)
    {
        this.serviceType = serviceType;
    }
    
    public int getSourceAddrTon()
    {
        return sourceAddrTon;
    }
    
    public void setSourceAddrTon(int sourceAddrTon)
    {
        this.sourceAddrTon = sourceAddrTon;
    }
    
    public int getSourceAddrNpi()
    {
        return sourceAddrNpi;
    }
    
    public void setSourceAddrNpi(int sourceAddrNpi)
    {
        this.sourceAddrNpi = sourceAddrNpi;
    }
    
    public String getSourceAddr()
    {
        return sourceAddr;
    }
    
    public void setSourceAddr(String sourceAddr)
    {
        this.sourceAddr = sourceAddr;
    }
    
    public int getDestAddrTon()
    {
        return destAddrTon;
    }
    
    public void setDestAddrTon(int destAddrTon)
    {
        this.destAddrTon = destAddrTon;
    }
    
    public int getDestAddrNpi()
    {
        return destAddrNpi;
    }
    
    public void setDestAddrNpi(int destAddrNpi)
    {
        this.destAddrNpi = destAddrNpi;
    }
    
    public String getDestinationAddr()
    {
        return destinationAddr;
    }
    
    public void setDestinationAddr(String destinationAddr)
    {
        this.destinationAddr = destinationAddr;
    }
    
    public int getEsmClass()
    {
        return esmClass;
    }
    
    public void setEsmClass(int esmClass)
    {
        this.esmClass = esmClass;
    }
    
    public int getProtocolId()
    {
        return protocolId;
    }
    
    public void setProtocolId(int protocolId)
    {
        this.protocolId = protocolId;
    }
    
    public int getPriorityFlag()
    {
        return priorityFlag;
    }
    
    public void setPriorityFlag(int priorityFlag)
    {
        this.priorityFlag = priorityFlag;
    }
    
    public String getScheduleDeliveryTime()
    {
        return scheduleDeliveryTime;
    }
    
    public void setScheduleDeliveryTime(String scheduleDeliveryTime)
    {
        this.scheduleDeliveryTime = scheduleDeliveryTime;
    }
    
    public String getValidityPeriod()
    {
        return validityPeriod;
    }
    
    public void setValidityPeriod(String validityPeriod)
    {
        this.validityPeriod = validityPeriod;
    }
    
    public int getRegisteredDelivery()
    {
        return registeredDelivery;
    }
    
    public void setRegisteredDelivery(int registeredDelivery)
    {
        this.registeredDelivery = registeredDelivery;
    }
    
    public int getReplaceIfPresentFlag()
    {
        return replaceIfPresentFlag;
    }
    
    public void setReplaceIfPresentFlag(int replaceIfPresentFlag)
    {
        this.replaceIfPresentFlag = replaceIfPresentFlag;
    }
    
    public int getDataCoding()
    {
        return dataCoding;
    }
    
    public void setDataCoding(int dataCoding)
    {
        this.dataCoding = dataCoding;
    }
    
    public int getSmDefaultMsgId()
    {
        return smDefaultMsgId;
    }
    
    public void setSmDefaultMsgId(int smDefaultMsgId)
    {
        this.smDefaultMsgId = smDefaultMsgId;
    }
    
    public int getSmLength()
    {
        return smLength;
    }
    
    public void setSmLength(int smLength)
    {
        this.smLength = smLength;
    }
    
    public String getShortMessage()
    {
        return shortMessage;
    }
    
    public void setShortMessage(String shortMessage)
    {
        this.shortMessage = shortMessage;
    }
}
