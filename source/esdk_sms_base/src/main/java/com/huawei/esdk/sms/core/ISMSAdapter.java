package com.huawei.esdk.sms.core;

import com.huawei.esdk.sms.exception.SDKSMSException;
import com.huawei.esdk.sms.model.SMSMessage;

public interface ISMSAdapter
{
    void setSMSAdapterId(String smsAdapterId);
    
    String getSMSAdapterId();
    
    ErrorInfo<String> sendSMSMessage(SMSMessage smsMessage) throws SDKSMSException;
    
    void registerRecvCallback(IRecvSMSCallback recvSMSCallback);
}
