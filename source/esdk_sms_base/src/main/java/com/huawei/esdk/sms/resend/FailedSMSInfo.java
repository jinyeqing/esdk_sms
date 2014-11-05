package com.huawei.esdk.sms.resend;

import com.huawei.esdk.sms.core.ISMSAdapter;
import com.huawei.esdk.sms.model.SMSMessage;

public class FailedSMSInfo
{
    private SMSMessage smsMessage;
    
    private ISMSAdapter smsAdapter;

    public SMSMessage getSmsMessage()
    {
        return smsMessage;
    }

    public void setSmsMessage(SMSMessage smsMessage)
    {
        this.smsMessage = smsMessage;
    }

    public ISMSAdapter getSmsAdapter()
    {
        return smsAdapter;
    }

    public void setSmsAdapter(ISMSAdapter smsAdapter)
    {
        this.smsAdapter = smsAdapter;
    }
}
