package com.huawei.esdk.sms.processor;

import com.huawei.esdk.sms.model.SMSMessage;

public interface Processor
{
    void processSms(SMSMessage smsMessage) throws Exception;
    
    void reProcessSms(SMSMessage smsMessage) throws Exception;
    
    SMSMessage sendSmsRequest(SMSMessage smsMessage) throws Exception;
    
}
