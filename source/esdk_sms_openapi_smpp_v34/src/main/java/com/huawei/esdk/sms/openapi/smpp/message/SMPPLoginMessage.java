package com.huawei.esdk.sms.openapi.smpp.message;

import com.huawei.esdk.platform.common.utils.BytesUtils;

public class SMPPLoginMessage extends SMPPPDU
{
    private String systemId;
    
    private String password;
    
    private String systemType;
    
    private int interfaceVersion;
    
    private int addrTon;
    
    private int addrNpi;
    
    private String addressRange;
    
    public SMPPLoginMessage()
    {
    }
    
    public SMPPLoginMessage(int loginType, String systemId, String password, String systemType, byte interfaceVersion,
        int addrTon, int addrNpi, String addressRange)
        throws IllegalArgumentException
    {
        setCommandId(loginType);
        this.systemId = systemId;
        this.password = password;
        this.systemType = systemType;
        this.interfaceVersion = interfaceVersion;
        this.addrTon = addrTon;
        this.addrNpi = addrNpi;
        this.addressRange = addressRange;
    }
    
    public void pack()
    {
        if (getCommandId() != 1 && getCommandId() != 2 && getCommandId() != 9)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("CONNECT_INPUT_ERROR")
                .append(":loginCommandId ")
                .append("OTHER_ERROR")
                .toString());
        }
        if (BytesUtils.getBytes(systemId).length > 15)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("CONNECT_INPUT_ERROR")
                .append(":systemId ")
                .append("STRING_LENGTH_GREAT")
                .append("15")
                .toString());
        }
        if (BytesUtils.getBytes(password).length > 8)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("CONNECT_INPUT_ERROR")
                .append(":password ")
                .append("STRING_LENGTH_GREAT")
                .append("8")
                .toString());
        }
        if (BytesUtils.getBytes(systemType).length > 12)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("CONNECT_INPUT_ERROR")
                .append(":systemType ")
                .append("STRING_LENGTH_GREAT")
                .append("12")
                .toString());
        }
        if (BytesUtils.getBytes(addressRange).length > 40)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("CONNECT_INPUT_ERROR")
                .append(":addressRange ")
                .append("STRING_LENGTH_GREAT")
                .append("40")
                .toString());
        }
        
        int len =
            23 + BytesUtils.getBytes(systemId).length + BytesUtils.getBytes(password).length + BytesUtils.getBytes(systemType).length
                + BytesUtils.getBytes(addressRange).length;
        byte[] buf = new byte[len];
        setCommandLength(len);
        setCommandStatus(0);
        int pos = 16;
        System.arraycopy(BytesUtils.getBytes(systemId), 0, buf, pos, BytesUtils.getBytes(systemId).length);
        pos = pos + BytesUtils.getBytes(systemId).length + 1;
        System.arraycopy(BytesUtils.getBytes(password), 0, buf, pos, BytesUtils.getBytes(password).length);
        pos = pos + BytesUtils.getBytes(password).length + 1;
        System.arraycopy(BytesUtils.getBytes(systemType), 0, buf, pos, BytesUtils.getBytes(systemType).length);
        pos = pos + BytesUtils.getBytes(systemType).length + 1;
        buf[pos] = (byte)interfaceVersion;
        pos++;
        buf[pos] = (byte)addrTon;
        pos++;
        buf[pos] = (byte)addrNpi;
        pos++;
        System.arraycopy(BytesUtils.getBytes(addressRange), 0, buf, pos, BytesUtils.getBytes(addressRange).length);
        setByteData(buf);
    }
    
    public void unpack()
    {
        int pos = 16;
        //retrieve system id
        systemId = getNullEndString(getByteData(), pos, "");
        pos += BytesUtils.getBytes(systemId).length + 1;
        //password
        password = getNullEndString(getByteData(), pos, "");
        pos += BytesUtils.getBytes(password).length + 1;
        //system type
        systemType = getNullEndString(getByteData(), pos, "");
        pos += BytesUtils.getBytes(systemType).length + 1;
        
        interfaceVersion = getByteData()[pos++];
        
        addrTon = getByteData()[pos++];
        
        addrNpi = getByteData()[pos++];
        
        addressRange = getNullEndString(getByteData(), pos, "");
    }
    
    @Override
    public String toString()
    {
        return "SMPPLoginMessage [systemId=" + systemId + ", password=******, systemType=" + systemType
            + ", interfaceVersion=" + interfaceVersion + ", addrTon=" + addrTon + ", addrNpi=" + addrNpi
            + ", addressRange=" + addressRange + ", toString()=" + super.toString() + "]";
    }
    
    public String getSystemId()
    {
        return systemId;
    }
    
    public void setSystemId(String systemId)
    {
        this.systemId = systemId;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getSystemType()
    {
        return systemType;
    }
    
    public void setSystemType(String systemType)
    {
        this.systemType = systemType;
    }
    
    public int getInterfaceVersion()
    {
        return interfaceVersion;
    }
    
    public void setInterfaceVersion(int interfaceVersion)
    {
        this.interfaceVersion = interfaceVersion;
    }
    
    public int getAddrTon()
    {
        return addrTon;
    }
    
    public void setAddrTon(int addrTon)
    {
        this.addrTon = addrTon;
    }
    
    public int getAddrNpi()
    {
        return addrNpi;
    }
    
    public void setAddrNpi(int addrNpi)
    {
        this.addrNpi = addrNpi;
    }
    
    public String getAddressRange()
    {
        return addressRange;
    }
    
    public void setAddressRange(String addressRange)
    {
        this.addressRange = addressRange;
    }
}