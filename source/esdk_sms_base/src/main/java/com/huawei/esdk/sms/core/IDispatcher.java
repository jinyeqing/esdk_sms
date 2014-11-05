package com.huawei.esdk.sms.core;

import com.huawei.esdk.sms.exception.SDKSMSException;
import com.huawei.esdk.sms.model.SMSMessage;

public interface IDispatcher
{
    String sendSMSMessage(SMSMessage smsMessage) throws SDKSMSException;
}
