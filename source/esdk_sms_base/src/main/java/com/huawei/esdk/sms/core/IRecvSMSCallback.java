package com.huawei.esdk.sms.core;

import com.huawei.esdk.sms.exception.SDKSMSException;
import com.huawei.esdk.sms.model.SMSDeliveryStatus;
import com.huawei.esdk.sms.model.SMSMessage;

public interface IRecvSMSCallback
{
    /**
     * 发送消息回执
     * 由eSDK实现
     * @param smsDeliveryStatus
     * @throws SDKSMSException
     */
    void onRecvReceipt(SMSDeliveryStatus smsDeliveryStatus) throws SDKSMSException;
    
    void onRecvSendResp() throws SDKSMSException;
    
    /**
     * eSDK收到ISV（短信网关）发来的短信
     * 由eSDK实现
     * @param smsMessage
     * @throws SDKSMSException
     */
    void onRecvIncomeSMS(SMSMessage smsMessage) throws SDKSMSException;
}
