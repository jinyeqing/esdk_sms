package com.huawei.esdk.sms.openapi.smpp.command.processor;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.exception.SDKException;
import com.huawei.esdk.sms.openapi.smpp.constant.SMPPConstant;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPEnquireLinkRespMessage;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPPDU;
import com.huawei.esdk.tcp.base.ISession;

public class SMPPEnquireLinkProcessor implements ISMPPCommandProcessor
{
    private static final Logger LOGGER = Logger.getLogger(SMPPEnquireLinkProcessor.class);
    
    @Override
    public void processSMPPPDU(SMPPPDU pdu, ISession session)
        throws SDKException
    {
        SMPPEnquireLinkRespMessage enquireRes = new SMPPEnquireLinkRespMessage();
        enquireRes.setCommandId(SMPPConstant.Enquire_Link_Rep_Command_Id);
        enquireRes.setCommandStatus(0);
        enquireRes.setSequenceNumber(pdu.getSequenceNumber());
        
        enquireRes.pack();
        try
        {
            LOGGER.debug("res enquireRes =" + enquireRes);
            session.send(enquireRes);
        }
        catch (IOException e)
        {
            LOGGER.error("", e);
        }
    }
}
