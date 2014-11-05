package com.huawei.esdk.sms.openapi.smpp.command.processor;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.huawei.esdk.platform.common.utils.ApplicationContextUtil;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.sms.core.impl.Dispatcher;
import com.huawei.esdk.sms.model.LongSMSMessage;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.utils.DateUtils;

public abstract class SMPPAbstractSubmitProcessor
{
    protected Dispatcher dispatcher = ApplicationContextUtil.getBean("dispatcher");
    
    protected final Map<String, List<LongSMSMessage>> SMS_CACHE = new Hashtable<String, List<LongSMSMessage>>();
    
    protected final Map<String, Integer> SMS_SEQ = new HashMap<String, Integer>();
    
    /**
     * To generated the short message identifier.
     * 
     * Base on SMPP specification v3.4, the max length is 65.
     * 
     * @return The generated short message identifier.
     */
    protected synchronized String generateMessageId()
    {
        String key = DateUtils.formatDate(DateUtils.DT_FT_YYYYMMDDHHMISS);
        Integer seq = SMS_SEQ.get(key);
        if (null == seq)
        {
            seq = 1;
        }
        else
        {
            seq = seq + 1;
        }
        
        SMS_SEQ.clear();
        SMS_SEQ.put(key, seq);
        
        return DateUtils.formatDate(DateUtils.DT_FT_YYYYMMDDHHMISS)
            + StringUtils.formatNumber(seq.toString(), 4, true, '0');
    }
    
    protected String populateKey(LongSMSMessage longSmsMessage)
    {
        StringBuilder sb =
            new StringBuilder(String.valueOf(Math.abs(longSmsMessage.getRandom()))).append(longSmsMessage.getCount())
                .append(longSmsMessage.getSrcId())
                .append(longSmsMessage.getDestIdAsString());
        
        return sb.toString();
    }
    
    protected void buildLongSMSMessage(LongSMSMessage longSMSMessage, String srcAddr, String[] destAddrs, int dataCoding,
        byte[] byteContent)
        throws UnsupportedEncodingException
    {
        buildSMSMessage(longSMSMessage, srcAddr, destAddrs, dataCoding, byteContent);
    }
    
    protected SMSMessage buildSMSMessage(SMSMessage smsMessage, String srcAddr, String[] destAddrs, int dataCoding,
        byte[] byteContent)
        throws UnsupportedEncodingException
    {
        smsMessage.setMsgTime(DateUtils.getCurrentDateTime());
        smsMessage.setSrcId(srcAddr);
        smsMessage.setDestId(destAddrs);
        smsMessage.setEncode(String.valueOf(dataCoding));
        byte[] content;
        if (isLongSMS(byteContent))
        {
            content = new byte[byteContent.length - 6];
            System.arraycopy(byteContent, 6, content, 0, content.length);
        }
        else
        {
            content = byteContent;
        }
        
        if (8 == dataCoding)
        {
            smsMessage.setContent(new String(content, "UTF-16BE"));
        }
        else if (15 == dataCoding)
        {
            smsMessage.setContent(new String(content, "GBK"));
        }
        else
        {
            smsMessage.setContent(new String(content, "UTF-8"));
        }
        
        return smsMessage;
    }
    
    /**
     * 判断是否为长短信
     */
    protected boolean isLongSMS(byte[] byteContent)
    {
        if (5 == byteContent[0] && 0 == byteContent[1] && 3 == byteContent[2])
        {
            return true;
        }
        
        return false;
    }
}
