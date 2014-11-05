package com.huawei.esdk.sms.openapi.smpp.command.processor;

import com.huawei.esdk.platform.common.exception.SDKException;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPPDU;
import com.huawei.esdk.tcp.base.ISession;

public interface ISMPPCommandProcessor
{
    void processSMPPPDU(SMPPPDU pdu, ISession session) throws SDKException;
}
