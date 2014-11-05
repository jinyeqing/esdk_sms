package com.huawei.esdk.sms.model;

public class SMSubmitRes
{
    private int resultCode;
    
    private String messageId;
    
    private String reserve1;
    
    private String reserve2;
    
    private String errorMessage;
    
    public int getResultCode()
    {
        return resultCode;
    }
    
    public void setResultCode(int resultCode)
    {
        this.resultCode = resultCode;
    }
    
    public String getMessageId()
    {
        return messageId;
    }
    
    public void setMessageId(String messageId)
    {
        this.messageId = messageId;
    }
    
    public String getReserve1()
    {
        return this.reserve1;
    }
    
    public void setReserve1(String reserve1)
    {
        this.reserve1 = reserve1;
    }
    
    public String getReserve2()
    {
        return reserve2;
    }
    
    public void setReserve2(String reserve2)
    {
        this.reserve2 = reserve2;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
}
