package com.huawei.esdk.sms.openapi.smpp.command.processor;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.exception.SDKException;
import com.huawei.esdk.sms.openapi.smpp.constant.SMPPConstant;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPLoginMessage;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPLoginRespMessage;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPPDU;
import com.huawei.esdk.sms.openapi.smpp.user.SMPPUserService;
import com.huawei.esdk.sms.openapi.smpp.user.User;
import com.huawei.esdk.tcp.base.ISession;

public class SMPPLoginProcessor implements ISMPPCommandProcessor
{
    private static final Logger LOGGER = Logger.getLogger(SMPPLoginProcessor.class);
    
    @Override
    public void processSMPPPDU(SMPPPDU pdu, ISession session) throws SDKException
    {
        if (!(pdu instanceof SMPPLoginMessage))
        {
            return;
        }
        SMPPLoginMessage loginReq = (SMPPLoginMessage)pdu;
        SMPPLoginRespMessage loginRes = new SMPPLoginRespMessage();
        
        int commandId = loginReq.getCommandId();
        //Header
        if (SMPPConstant.Bind_Receiver_Command_Id == commandId)
        {
            loginRes.setCommandId(SMPPConstant.Bind_Receiver_Rep_Command_Id);
        }
        else if (SMPPConstant.Bind_Transceiver_Command_Id == commandId)
        {
            loginRes.setCommandId(SMPPConstant.Bind_Transceiver_Rep_Command_Id);
        }
        else
        {
            loginRes.setCommandId(SMPPConstant.Bind_Transmitter_Rep_Command_Id);
        }
        
        User user = SMPPUserService.getInstance().findUser(loginReq.getSystemId());
        if (null == user)
        {
            loginRes.setCommandStatus(0x0000000F);
        }
        else
        {
            if (null != loginReq.getPassword() && loginReq.getPassword().equals(user.getPwd()))
            {
                loginRes.setCommandStatus(0);
                loginRes.setSequenceNumber(loginReq.getSequenceNumber());
                session.setAccount(loginReq.getSystemId());
                session.setHasLogin(true);
            }
            else
            {
                loginRes.setCommandStatus(0x0000000E);
            }
        }
        
        //Body
        loginRes.setSystemId(loginReq.getSystemId());
        
        loginRes.pack();
        try
        {
            LOGGER.debug("The Login response PDU = " + loginRes);
            session.send(loginRes);
            
            if (0 != loginRes.getCommandStatus())
            {
                session.stop();
            }
        }
        catch (IOException e)
        {
            LOGGER.error("", e);
        }
    }
}
