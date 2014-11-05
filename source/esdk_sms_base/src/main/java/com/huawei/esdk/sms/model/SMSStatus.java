package com.huawei.esdk.sms.model;

public class SMSStatus
{
    private String srcMobileNumber;
    
    private String destMobileNumber;
    
    private String status;
    
    private String reservedField1;
    
    private String reservedField2;
    
    public String getSrcMobileNumber()
    {
        return srcMobileNumber;
    }
    
    public void setSrcMobileNumber(String srcMobileNumber)
    {
        this.srcMobileNumber = srcMobileNumber;
    }
    
    public String getDestMobileNumber()
    {
        return destMobileNumber;
    }
    
    public void setDestMobileNumber(String destMobileNumber)
    {
        this.destMobileNumber = destMobileNumber;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getReservedField1()
    {
        return reservedField1;
    }
    
    public void setReservedField1(String reservedField1)
    {
        this.reservedField1 = reservedField1;
    }
    
    public String getReservedField2()
    {
        return reservedField2;
    }
    
    public void setReservedField2(String reservedField2)
    {
        this.reservedField2 = reservedField2;
    }
}
