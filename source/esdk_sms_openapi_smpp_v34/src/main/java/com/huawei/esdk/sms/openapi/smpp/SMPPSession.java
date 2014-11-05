package com.huawei.esdk.sms.openapi.smpp;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.exception.SDKException;
import com.huawei.esdk.sms.openapi.smpp.command.processor.ISMPPCommandProcessor;
import com.huawei.esdk.sms.openapi.smpp.command.processor.SMPPDeliverRespProcessor;
import com.huawei.esdk.sms.openapi.smpp.command.processor.SMPPEnquireLinkProcessor;
import com.huawei.esdk.sms.openapi.smpp.command.processor.SMPPLoginProcessor;
import com.huawei.esdk.sms.openapi.smpp.command.processor.SMPPSubmitMultiProcessor;
import com.huawei.esdk.sms.openapi.smpp.command.processor.SMPPSubmitProcessor;
import com.huawei.esdk.sms.openapi.smpp.command.processor.SMPPUnbindProcessor;
import com.huawei.esdk.sms.openapi.smpp.constant.SMPPConstant;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPPDU;
import com.huawei.esdk.tcp.base.AbstractSession;
import com.huawei.esdk.tcp.base.TCPServerEngine;
import com.huawei.esdk.tcp.base.pdu.PDU;

public class SMPPSession extends AbstractSession
{
    private static final Logger LOGGER = Logger.getLogger(SMPPSession.class);
    
    private static final Map<Integer, ISMPPCommandProcessor> COMMAND_PROCESSOR_MAPPING =
        new HashMap<Integer, ISMPPCommandProcessor>();
    
    private String account;
    
    public SMPPSession(TCPServerEngine tcpServerEngine)
    {
        super(new SMPPByteDataProcessor(), tcpServerEngine);
        SMPPLoginProcessor smppProcessor = new SMPPLoginProcessor();
        COMMAND_PROCESSOR_MAPPING.put(SMPPConstant.Bind_Receiver_Command_Id, smppProcessor);
        COMMAND_PROCESSOR_MAPPING.put(SMPPConstant.Bind_Transceiver_Command_Id, smppProcessor);
        COMMAND_PROCESSOR_MAPPING.put(SMPPConstant.Bind_Transmitter_Command_Id, smppProcessor);
        COMMAND_PROCESSOR_MAPPING.put(SMPPConstant.Submit_Command_Id, new SMPPSubmitProcessor());
        COMMAND_PROCESSOR_MAPPING.put(SMPPConstant.Submit_Multi_Id, new SMPPSubmitMultiProcessor());
        COMMAND_PROCESSOR_MAPPING.put(SMPPConstant.Deliver_Rep_Command_Id, new SMPPDeliverRespProcessor());
        COMMAND_PROCESSOR_MAPPING.put(SMPPConstant.Enquire_Link_Command_Id, new SMPPEnquireLinkProcessor());
        COMMAND_PROCESSOR_MAPPING.put(SMPPConstant.Unbind_Command_Id, new SMPPUnbindProcessor());
    }
    
    @Override
    public Object getAccount()
    {
        return this.account;
    }
    
    @Override
    public void setAccount(Object account)
    {
        this.account = (String)account;
    }
    
    @Override
    protected void processPDU(PDU pdu)
    {
        LOGGER.debug("processing pdu=" + pdu + " from " + getRemoteIP());
        SMPPPDU smppReq = (SMPPPDU)pdu;
        int commandId = smppReq.getCommandId();
        try
        {
            ISMPPCommandProcessor processor = COMMAND_PROCESSOR_MAPPING.get(commandId);
            if (null != processor)
            {
                processor.processSMPPPDU(smppReq, this);
            }
            else
            {
                LOGGER.warn("This is not a processor for commandId = " + commandId);
            }
        }
        catch (SDKException e)
        {
            LOGGER.error("", e);
        }
    }
    
    
}
