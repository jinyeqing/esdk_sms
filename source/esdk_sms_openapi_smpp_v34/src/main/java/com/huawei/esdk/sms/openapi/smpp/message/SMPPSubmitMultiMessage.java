package com.huawei.esdk.sms.openapi.smpp.message;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.huawei.esdk.platform.common.utils.BytesUtils;

public class SMPPSubmitMultiMessage extends SMPPPDU
{
    private String serviceType;
    
    private int sourceAddrTon;
    
    private int sourceAddrNpi;
    
    private String sourceAddr;
    
    private int numberOfDests;
    
    private DestAddresses[] destAddresses;
    
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
    
    private byte[] shortMessageByte;
    
    public void pack()
    {
        //Now is empty as it's used as a server
    }
    
    public void unpack()
    {
        int pos = SMPP_HEADER_LENGTH;
        byte[] buf = getByteData();
        
        serviceType = getNullEndString(buf, pos, "");
        pos += BytesUtils.getBytes(serviceType).length + 1;
        sourceAddrTon = buf[pos++];
        sourceAddrNpi = buf[pos++];
        sourceAddr = getNullEndString(buf, pos, "");
        pos += BytesUtils.getBytes(sourceAddr).length + 1;
        numberOfDests = buf[pos++];
        pos = parseDestAddresses(pos, buf);
        esmClass = buf[pos++];
        protocolId = buf[pos++];
        priorityFlag = buf[pos++];
        scheduleDeliveryTime = getNullEndString(buf, pos, "");
        pos += BytesUtils.getBytes(scheduleDeliveryTime).length + 1;
        validityPeriod = getNullEndString(buf, pos, "");
        pos += BytesUtils.getBytes(validityPeriod).length + 1;
        registeredDelivery = buf[pos++];
        replaceIfPresentFlag = buf[pos++];
        dataCoding = buf[pos++];
        smDefaultMsgId = buf[pos++];
        smLength = buf[pos++];
        if (smLength < 0)
        {
            smLength = 256 + smLength;
        }
        byte[] shortMessageByte = new byte[smLength];
        System.arraycopy(buf, pos, shortMessageByte, 0, shortMessageByte.length);
        //Following line is commented for fixing FindBug issue
//        pos += shortMessageByte.length;
        this.shortMessageByte = shortMessageByte;
        
        try
        {
            shortMessage = new String(shortMessageByte, getCharset(dataCoding));
        }
        catch (UnsupportedEncodingException e)
        {
            try
            {
                shortMessage = new String(shortMessageByte, "UTF-8");
            }
            catch (UnsupportedEncodingException e1)
            {
                shortMessage = "";
            }
        }
    }
    
    private int parseDestAddresses(int offset, byte[] buf)
    {
        DestAddresses item;
        List<DestAddresses> list = new ArrayList<DestAddresses>();
        int destFlag;
        for (int i = 0; i < numberOfDests; i++)
        {
            destFlag = buf[offset++];
            item = new DestAddresses();
            item.setDesFlag(destFlag);
            if (1 == destFlag)
            {
                item.setDestAddrTon(buf[offset++]);
                item.setDestAddrNpi(buf[offset++]);
                item.setDestinationAddr(getNullEndString(buf, offset, ""));
                offset += BytesUtils.getBytes(item.getDestinationAddr()).length + 1;
            }
            else if (2 == destFlag)
            {
                item.setDlName(getNullEndString(buf, offset, ""));
                offset += BytesUtils.getBytes(item.getDlName()).length + 1;
            }
            
            list.add(item);
        }
        destAddresses = new DestAddresses[numberOfDests];
        list.toArray(destAddresses);
        
        return offset;
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
    
    public int getNumberOfDests()
    {
        return numberOfDests;
    }
    
    public void setNumberOfDests(int numberOfDests)
    {
        this.numberOfDests = numberOfDests;
    }
    
    public DestAddresses[] getDestAddresses()
    {
        return destAddresses;
    }
    
    public void setDestAddresses(DestAddresses[] destAddresses)
    {
        this.destAddresses = destAddresses;
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

    public byte[] getShortMessageByte()
    {
        return shortMessageByte;
    }

    public void setShortMessageByte(byte[] shortMessageByte)
    {
        this.shortMessageByte = shortMessageByte;
    }
}
