package com.huawei.esdk.sms.openapi.smpp.command.processor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.exception.SDKException;
import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.sms.exception.SDKSMSException;
import com.huawei.esdk.sms.model.LongSMSMessage;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.openapi.smpp.constant.SMPPConstant;
//import com.huawei.esdk.sms.openapi.smpp.message.SMPPDeliverMessage;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPPDU;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPSubmitMultiMessage;
import com.huawei.esdk.sms.openapi.smpp.message.SMPPSubmitMultiRespMessage;
import com.huawei.esdk.tcp.base.ISession;

public class SMPPSubmitMultiProcessor extends SMPPAbstractSubmitProcessor implements ISMPPCommandProcessor
{
    private static final Logger LOGGER = Logger.getLogger(SMPPSubmitMultiProcessor.class);
    
    @Override
    public void processSMPPPDU(SMPPPDU pdu, ISession session)
        throws SDKException
    {
        if (!(pdu instanceof SMPPSubmitMultiMessage))
        {
            return;
        }
        SMPPSubmitMultiMessage submitMultiReq = (SMPPSubmitMultiMessage)pdu;
        
        SMPPSubmitMultiRespMessage submitMultiRes = new SMPPSubmitMultiRespMessage();
        //SMPPDeliverMessage deliverReq = null;
        
        submitMultiRes.setCommandId(SMPPConstant.Submit_Multi_Resp_Id);
        submitMultiRes.setCommandStatus(0);
        submitMultiRes.setSequenceNumber(submitMultiReq.getSequenceNumber());
        
        String messageId = null;
        try
        {
            messageId = processSubmitMessage(submitMultiReq);
        }
        catch (Exception e)
        {
            LOGGER.error("", e);
            submitMultiRes.setCommandStatus(0x00000008);//System Error
        }
        if (null == messageId)
        {
            messageId = generateMessageId();
        }
        
        submitMultiRes.setMessageId(messageId);
        
        //        if (1 == submitMultiReq.getRegisteredDelivery())
        //        {
        //            deliverReq = new SMPPDeliverMessage();
        //            deliverReq.setCommandId(SMPPConstant.Deliver_Command_Id);
        //        }
        
        try
        {
            submitMultiRes.pack();
            LOGGER.debug("res submitMultiRes =" + submitMultiRes);
            session.send(submitMultiRes);
            //            //TODO deliver receipt 
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
    
    private String processSubmitMessage(SMPPSubmitMultiMessage submit)
        throws UnsupportedEncodingException, SDKSMSException
    {
        String messageId = null;
        String[] destAddrs = new String[submit.getDestAddresses().length];
        for (int index = 0; index < submit.getDestAddresses().length; index++)
        {
            destAddrs[index] = submit.getDestAddresses()[index].getDestinationAddr();
        }
        
        if (isLongSMS(submit.getShortMessageByte()))
        {
            LongSMSMessage lSms =
                new LongSMSMessage(submit.getShortMessageByte()[3], submit.getShortMessageByte()[4],
                    submit.getShortMessageByte()[5]);
            //        lSms.setId(messageId);
            buildLongSMSMessage(lSms,
                submit.getSourceAddr(),
                destAddrs,
                submit.getDataCoding(),
                submit.getShortMessageByte());
            LOGGER.debug("The incoming long message:" + lSms.getSMS4Logging());
            PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + lSms.getSMS4Logging() + "] is received");
            synchronized(this)
            {
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
        }
        else
        {
            SMSMessage sms = new SMSMessage();
            buildSMSMessage(sms,
                submit.getSourceAddr(),
                destAddrs,
                submit.getDataCoding(),
                submit.getShortMessageByte());
            LOGGER.debug("The incoming message:" + sms.getSMS4Logging());
            PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + sms.getSMS4Logging() + "] is received");
            // Send Normal SMS
            messageId = dispatcher.sendSMSMessage(sms);
        }
        
        return messageId;
    }
}
