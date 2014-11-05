package com.huawei.esdk.sms.core;

public class ErrorInfo<T>
{
    private T value;
    
    public T getValue()
    {
        return value;
    }
    
    public void setValue(T value)
    {
        this.value = value;
    }
}
