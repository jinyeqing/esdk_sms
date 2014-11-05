package com.huawei.esdk.sms.openapi.smpp.command.processor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.exception.SDKException;
import com.huawei.esdk.platform.common.utils.BytesUtils;
import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.sms.exception.SDKSMSException;
import com.huawei.esdk.sms.model.LongSMSMessage;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.openapi.smpp.constant.SMPPConstant;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPDeliverMessage;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPPDU;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPSubmitMessage;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPSubmitRespMessage;
import com.huawei.esdk.sms.utils.DateUtils;
import com.huawei.esdk.tcp.base.ISession;

public class SMPPSubmitProcessor extends SMPPAbstractSubmitProcessor implements ISMPPCommandProcessor
{
    private static final Logger LOGGER = Logger.getLogger(SMPPSubmitProcessor.class);
    
    @Override
    public void processSMPPPDU(SMPPPDU pdu, ISession session)
        throws SDKException
    {
        if (!(pdu instanceof SMPPSubmitMessage))
        {
            return;
        }
        SMPPSubmitMessage submitReq = (SMPPSubmitMessage)pdu;
        SMPPSubmitRespMessage submitRes = new SMPPSubmitRespMessage();
//        SMPPDeliverMessage deliverReq = null;
        
        submitRes.setCommandId(SMPPConstant.Submit_Rep_Command_Id);
        submitRes.setCommandStatus(0);
        submitRes.setSequenceNumber(submitReq.getSequenceNumber());
        String messageId = null;
        try
        {
            messageId = processSubmitMessage(submitReq);
        }
        catch (Exception e)
        {
            LOGGER.error("", e);
            //0x000000FF - Unknown Error
            //0x00000008 - System Error
            submitRes.setCommandStatus(0x00000008);
        }
        if (null == messageId)
        {
            messageId = generateMessageId();
        }
        submitRes.setMessageId(messageId);
        
//        if (1 == submitReq.getRegisteredDelivery())
//        {
//            deliverReq = buildSMPPDeliverMessage(messageId, submitReq);
//        }
        
        try
        {
            submitRes.pack();
            LOGGER.debug("res submitRes =" + submitRes);
            session.send(submitRes);
//            if (null != deliverReq)
//            {
//                deliverReq.pack();
//                session.send(deliverReq);
//            }
        }
        catch (IOException e)
        {
            LOGGER.error("", e);
        }
    }
    
    protected SMPPDeliverMessage buildSMPPDeliverMessage(String messageId, SMPPSubmitMessage submitReq)
    {
        //Do not support delivery receipt for V1R3C00
        SMPPDeliverMessage deliverReq = new SMPPDeliverMessage();
        deliverReq.setCommandId(SMPPConstant.Deliver_Command_Id);
        deliverReq.setServiceType(submitReq.getServiceType());
        //TODO
        deliverReq.setSourceAddrTon(submitReq.getSourceAddrTon());
        deliverReq.setSourceAddrNpi(submitReq.getSourceAddrNpi());
        deliverReq.setSourceAddr(submitReq.getSourceAddr());
        deliverReq.setDestAddrTon(submitReq.getDestAddrTon());
        deliverReq.setDestAddrNpi(submitReq.getDestAddrNpi());
        deliverReq.setDestinationAddr(submitReq.getDestinationAddr());
        //x x 0 0 0 1 x x Short Message contains SMSC Delivery Receipt
        deliverReq.setEsmClass(4);
        deliverReq.setProtocolId(submitReq.getProtocolId());
        deliverReq.setPriorityFlag(submitReq.getPriorityFlag());
        deliverReq.setRegisteredDelivery(0);
        deliverReq.setDataCoding(0);
        
        //Format:
        //id:IIIIIIIIII sub:SSS dlvrd:DDD submit date:YYMMDDhhmm done date:YYMMDDhhmm stat:DDDDDDD err:E Text: . . . . . . . . .
        StringBuilder receiptMsg = new StringBuilder("id:");
        receiptMsg.append(messageId);
        receiptMsg.append(" sub:").append("001");
        receiptMsg.append(" dlvrd:").append("001");
        receiptMsg.append(" submit date:").append(DateUtils.formatDate(DateUtils.DT_FT_YYYYMMDDHHMI));
        receiptMsg.append(" done date:").append(DateUtils.formatDate(DateUtils.DT_FT_YYYYMMDDHHMI));//TODO After send to determine the date
        receiptMsg.append(" stat:").append("DELIVRD");
        receiptMsg.append(" err:").append("");
        if (submitReq.getShortMessage().length() > 20)
        {
            receiptMsg.append(" Text:").append(submitReq.getShortMessage().subSequence(0, 20));
        }
        else
        {
            receiptMsg.append(" Text:").append(submitReq.getShortMessage());
        }
        
        deliverReq.setSmLength(BytesUtils.getBytes(receiptMsg.toString()).length);
        deliverReq.setShortMessage(receiptMsg.toString());
        
        return deliverReq;
    }
    
    private String processSubmitMessage(SMPPSubmitMessage submit)
        throws UnsupportedEncodingException, SDKSMSException
    {
        String messageId = null;
        if (isLongSMS(submit.getShortMessageByte()))
        {
            LongSMSMessage lSms =
                new LongSMSMessage(submit.getShortMessageByte()[3], submit.getShortMessageByte()[4],
                    submit.getShortMessageByte()[5]);
//            lSms.setId(messageId);
            buildLongSMSMessage(lSms, submit.getSourceAddr(), new String[]{submit.getDestinationAddr()}, submit.getDataCoding(),
                submit.getShortMessageByte());
            PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + lSms.getSMS4Logging() + "] is received");
            List<LongSMSMessage> list = SMS_CACHE.get(populateKey(lSms));
            if (list == null)
            {
                list = new ArrayList<LongSMSMessage>();
                SMS_CACHE.put(populateKey(lSms), list);
            }
            list.add(lSms);
            // Merge the long SMS
            if (list.size() == lSms.getCount())
            {
                Collections.sort(list);
                StringBuilder sb = new StringBuilder();
                for (LongSMSMessage item : list)
                {
                    sb.append(item.getContent());
                }
                lSms.setContent(sb.toString());
                SMS_CACHE.remove(populateKey(lSms));
                // Send Long SMS (which is merged from multiple SMSs)
                messageId = dispatcher.sendSMSMessage(lSms);
            }
        }
        else
        {
            SMSMessage sms = new SMSMessage();
            buildSMSMessage(sms, submit.getSourceAddr(), new String[]{submit.getDestinationAddr()}, submit.getDataCoding(),
                submit.getShortMessageByte());
            PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + sms.getSMS4Logging() + "] is received");
            // Send Normal SMS
            messageId = dispatcher.sendSMSMessage(sms);
        }
        
        return messageId;
    }
}
