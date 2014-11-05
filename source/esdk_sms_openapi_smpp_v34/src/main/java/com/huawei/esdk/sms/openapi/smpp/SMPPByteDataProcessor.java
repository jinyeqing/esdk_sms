package com.huawei.esdk.sms.openapi.smpp;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.utils.TypeConvertUtils;
import com.huawei.esdk.sms.openapi.smpp.constant.SMPPConstant;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPDeliverRespMessage;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPEnquireLinkMessage;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPLoginMessage;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPPDU;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPSubmitMessage;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPSubmitMultiMessage;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPUnbindMessage;
import com.huawei.esdk.tcp.base.ByteDataProcessor;
import com.huawei.esdk.tcp.base.pdu.PDU;

public class SMPPByteDataProcessor implements ByteDataProcessor
{
    private static final Logger LOGGER = Logger.getLogger(SMPPByteDataProcessor.class);
    
    @Override
    public List<PDU> processByteData(byte[] byteData)
    {
        List<PDU> result = new ArrayList<PDU>();
        
        if (null != byteData && byteData.length >= 16)
        {
            int totalLength = 0;
            int length;
            byte[] pduBytes;
            while(totalLength < byteData.length)
            {
                length = TypeConvertUtils.byte2int(byteData, totalLength);
                pduBytes = new byte[length]; 
                System.arraycopy(byteData, totalLength, pduBytes, 0, length);
                
                totalLength += length;
                result.add(build(pduBytes));
            }
        }
        else
        {
            if (null != byteData)
            {
                LOGGER.warn("byteData.length = " + byteData.length);
            }
            else
            {
                LOGGER.debug("byteData is null");
            }
        }
        
        return result;
    }
    
    private PDU build(byte[] byteData)
    {
        SMPPPDU smppPDU = null;
        
        int commandId = TypeConvertUtils.byte2int(byteData, 4);
        
        if (commandId == SMPPConstant.Bind_Receiver_Command_Id
            || commandId == SMPPConstant.Bind_Transmitter_Command_Id
            || commandId == SMPPConstant.Bind_Transceiver_Command_Id)
        {
            smppPDU = new SMPPLoginMessage();
        }
        else if (commandId == SMPPConstant.Submit_Command_Id)
        {
            smppPDU = new SMPPSubmitMessage();
        }
        else if (commandId == SMPPConstant.Submit_Multi_Id)
        {
            smppPDU = new SMPPSubmitMultiMessage();
        }
        else if (commandId == SMPPConstant.Enquire_Link_Command_Id)
        {
            smppPDU = new SMPPEnquireLinkMessage();
        }
        else if (commandId == SMPPConstant.Unbind_Command_Id)
        {
            smppPDU = new SMPPUnbindMessage();
        }
        else if (commandId == SMPPConstant.Deliver_Rep_Command_Id)
        {
            smppPDU = new SMPPDeliverRespMessage();
        }
        else
        {
            smppPDU = new SMPPPDU();
        }
        
        //Retrieve command length
        smppPDU.setCommandLength(TypeConvertUtils.byte2int(byteData, 0));
        
        //Retrieve command id
        smppPDU.setCommandId(commandId);
        
        //Retrieve command status
        smppPDU.setCommandStatus(TypeConvertUtils.byte2int(byteData, 8));
        
        //Retrieve sequence number
        smppPDU.setSequenceNumber(TypeConvertUtils.byte2int(byteData, 12));
        
        byte[] bodyContent = new byte[byteData.length - 16];
        System.arraycopy(byteData, 16, bodyContent, 0, byteData.length - 16);
        
        smppPDU.setByteData(byteData);
        smppPDU.setBodyContent(bodyContent);
        smppPDU.unpack();
        
        return smppPDU;
    }
}
