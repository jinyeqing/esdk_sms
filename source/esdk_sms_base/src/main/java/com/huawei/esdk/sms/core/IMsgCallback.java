package com.huawei.esdk.sms.core;

import com.huawei.esdk.sms.model.SMSDeliveryStatus;
import com.huawei.esdk.sms.model.SMSMessage;

public interface IMsgCallback
{
    void onSmsDeliveryStatus(SMSDeliveryStatus smsDeliveryStatus);
    
    void onSmsReception(SMSMessage smsMessage);
}
