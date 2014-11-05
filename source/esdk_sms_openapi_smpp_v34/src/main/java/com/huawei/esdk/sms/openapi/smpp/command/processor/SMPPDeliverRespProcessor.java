package com.huawei.esdk.sms.openapi.smpp.command.processor;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.exception.SDKException;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPDeliverRespMessage;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPPDU;
import com.huawei.esdk.tcp.base.ISession;

public class SMPPDeliverRespProcessor implements ISMPPCommandProcessor
{
    private static final Logger LOGGER = Logger.getLogger(SMPPDeliverRespProcessor.class);
    
    @Override
    public void processSMPPPDU(SMPPPDU pdu, ISession session)
        throws SDKException
    {
        //Do nothing
        if (pdu instanceof SMPPDeliverRespMessage)
        {
            SMPPDeliverRespMessage deliverResp = (SMPPDeliverRespMessage) pdu;
            LOGGER.debug("deliverResp command status = " + deliverResp.getCommandStatus());
        }
    }
}
