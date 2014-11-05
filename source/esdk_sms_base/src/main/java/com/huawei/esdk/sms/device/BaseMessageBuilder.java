package com.huawei.esdk.sms.device;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.sms.constants.SMSConstants;
import com.huawei.esdk.sms.utils.SMSUtils;

public abstract class BaseMessageBuilder implements SMSConstants
{
    private Map<String, String> ENCODE_CHARSE_MAPPING;
    
    public BaseMessageBuilder()
    {
        String smppMapping = ConfigManager.getInstance().getValue(getMappingProp());
        if (StringUtils.isEmpty(smppMapping))
        {
            smppMapping = ConfigManager.getInstance().getValue("esdk.sms.data.coding.charset.mapping");
        }
        
        ENCODE_CHARSE_MAPPING = SMSUtils.getDataCodingCharsetMapping(smppMapping);
    }
    
    protected List<byte[]> splitLongMessage(String content, String charset, int normalSMMaxLength, int headerBit)
        throws UnsupportedEncodingException
    {
        if (6 != headerBit && 7 != headerBit)
        {
            throw new IllegalArgumentException("Short Message Header bit is not 6 or 7");
        }
        
        List<byte[]> smsList = new ArrayList<byte[]>();
        byte[] msgContent = content.getBytes(charset);
        if (msgContent.length > normalSMMaxLength)
        {
            // 大于normalSMSLength位就要转化成长短信
            // normalSMSLength字节去掉headerBit位消息头
            int subContentlength = (normalSMMaxLength - headerBit) / " ".getBytes(charset).length;
            int smsCount;
            if (content.length() % (subContentlength) == 0)
            {
                smsCount = content.length() / (subContentlength);
            }
            else
            {
                smsCount = content.length() / (subContentlength) + 1;
            }
            
            int random = getRandom();
            
            String messagePross = content;
            byte[] msgBody;
            byte[] sHead;
            byte[] msgContentByteSub;
            for (int i = smsCount; i > 0; i--)
            {
                // 从后向前的顺序添加各分段消息
                String msgContentStrSub = messagePross.substring((i - 1) * subContentlength);
                msgBody = msgContentStrSub.getBytes(charset);
                
                if (6 == headerBit)//6 Bit
                {
                    sHead = new byte[] {5, 0, 3, (byte)random, (byte)smsCount, (byte)(i)};
                }
                else
                {
                    //7 Bit
                    sHead = new byte[] {6, 8, 4, 0, (byte)random, (byte)smsCount, (byte)(i)};
                }
                
                msgContentByteSub = new byte[sHead.length + msgBody.length];
                
                System.arraycopy(sHead, 0, msgContentByteSub, 0, headerBit);
                System.arraycopy(msgBody, 0, msgContentByteSub, headerBit, msgBody.length);
                
                smsList.add(msgContentByteSub);
                
                if (i > 1)
                {
                    messagePross = messagePross.substring(0, (i - 1) * subContentlength);
                }
            }
            
            Collections.reverse(smsList);
        }
        else
        {
            throw new IllegalArgumentException("This message is a normal SMS, the content length is "
                + msgContent.length);
        }
        
        return smsList;
    }
    
    public String getCharset(int dataCoding)
    {
        String result = ENCODE_CHARSE_MAPPING.get(String.valueOf(dataCoding));
        if (null == result)
        {
            result = "utf-8";
        }
        
        return result;
    }
    
    public String encodeNumber(String mobilePhoneNumber)
    {
        String temp = ConfigManager.getInstance().getValue(getProtocolType() + ".src-sp-code-encoding-way");
        if ("0".equalsIgnoreCase(temp))
        {
            return SMSUtils.encodeSPNumber(getSpNumber(), mobilePhoneNumber);
        }
        else if ("1".equalsIgnoreCase(temp))
        {
            return mobilePhoneNumber;
        }
        else
        {
            return getSpNumber();
        }
    }
    
    public String decodeNumber(String number)
    {
        String temp = ConfigManager.getInstance().getValue(getProtocolType() + ".src-sp-code-encoding-way");
        
        if ("0".equalsIgnoreCase(temp))
        {
            return SMSUtils.decodeSPNumber(getSpNumber(), number);
        }
        else
        {
            return number;
        }
    }
    
    private int getRandom()
    {
        Random randomObj = new Random();
        int random = randomObj.nextInt(255);
        if (0 == random)
        {
            random += 2;
        }
        else if (1 == random)
        {
            random++;
        }
        
        return random;
    }
    
    protected abstract String getMappingProp();
    
    protected abstract String getSpNumber();
    
    protected String getProtocolType()
    {
        return "";
    }
}
