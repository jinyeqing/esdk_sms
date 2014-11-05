package com.huawei.esdk.sms.openapi.smpp.command.processor;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.exception.SDKException;
import com.huawei.esdk.sms.openapi.smpp.constant.SMPPConstant;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPPDU;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPUnbindRespMessage;
import com.huawei.esdk.tcp.base.ISession;

public class SMPPUnbindProcessor implements ISMPPCommandProcessor
{
    private static final Logger LOGGER = Logger.getLogger(SMPPUnbindProcessor.class);
    
    @Override
    public void processSMPPPDU(SMPPPDU pdu, ISession session)
        throws SDKException
    {
        SMPPUnbindRespMessage unbindRes = new SMPPUnbindRespMessage();
        unbindRes.setCommandId(SMPPConstant.Unbind_Rep_Command_Id);
        unbindRes.setCommandStatus(0);
        unbindRes.setSequenceNumber(pdu.getSequenceNumber());
        
        unbindRes.pack();
        try
        {
            session.send(unbindRes);
        }
        catch (IOException e)
        {
            LOGGER.error("", e);
        }
        
        session.stop();
    }
}
