package com.huawei.esdk.sms.openapi.smpp.message;

import com.huawei.esdk.platform.common.utils.BytesUtils;
import com.huawei.esdk.platform.common.utils.TypeConvertUtils;

public class DestAddresses
{
    private int desFlag;
    
    private int destAddrTon;
    
    private int destAddrNpi;
    
    private String destinationAddr;
    
    private String dlName;
    
    private int errorStatusCode;
    
    public void setDesFlag(int desFlag)
    {
        this.desFlag = desFlag;
    }
    
    public void setDestAddrTon(int destAddrTon)
    {
        this.destAddrTon = destAddrTon;
    }
    
    public void setDestAddrNpi(int destAddrNpi)
    {
        this.destAddrNpi = destAddrNpi;
    }
    
    public void setDestinationAddr(String destinationAddr)
    {
        this.destinationAddr = destinationAddr;
    }
    
    public void setDlName(String dlName)
    {
        this.dlName = dlName;
    }
    
    public void setErrorStatusCode(int errorStatusCode)
    {
        this.errorStatusCode = errorStatusCode;
    }
    
    public int getDesFlag()
    {
        return desFlag;
    }
    
    public int getDestAddrTon()
    {
        return destAddrTon;
    }
    
    public int getDestAddrNpi()
    {
        return destAddrNpi;
    }
    
    public String getDestinationAddr()
    {
        return destinationAddr;
    }
    
    public String getDlName()
    {
        return dlName;
    }
    
    public int getErrorStatusCode()
    {
        return errorStatusCode;
    }
    
    public byte[] getByte4ReqPack()
    {
        byte buf[] = null;
        int lenght = 0;
        if (desFlag == 1)
        {
            lenght = 3 + BytesUtils.getBytes(destinationAddr).length + 1;
            buf = new byte[lenght];
            buf[0] = (byte)desFlag;
            buf[1] = (byte)destAddrTon;
            buf[2] = (byte)destAddrNpi;
            System.arraycopy(BytesUtils.getBytes(destinationAddr), 0, buf, 3, BytesUtils.getBytes(destinationAddr).length);
        }
        else if (desFlag == 2)
        {
            lenght = 1 + BytesUtils.getBytes(dlName).length + 1;
            buf = new byte[lenght];
            buf[0] = (byte)desFlag;
            System.arraycopy(BytesUtils.getBytes(dlName), 0, buf, 1, BytesUtils.getBytes(dlName).length);
        }
        return buf;
    }
    
    public byte[] getByte4ResPack()
    {
        byte buf[] = null;
        int lenght = 0;
        lenght = 1 + 1 + BytesUtils.getBytes(destinationAddr).length + 1 + 4;
        buf = new byte[lenght];
        int pos = 0;
        buf[pos++] = (byte)destAddrTon;
        buf[pos++] = (byte)destAddrNpi;
        System.arraycopy(BytesUtils.getBytes(destinationAddr), 0, buf, 3, BytesUtils.getBytes(destinationAddr).length);
        pos += BytesUtils.getBytes(destinationAddr).length + 1;
        TypeConvertUtils.int2byte(this.errorStatusCode, buf, pos);
        return buf;
    }
}
