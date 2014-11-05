package com.huawei.esdk.sms.utils;

import java.util.Locale;
import java.util.Map;

import com.huawei.esdk.platform.common.utils.StringUtils;

public abstract class SMSUtils
{
    /**
     * 将号码前缀与用户号码连接为完整的源号码
     * 
     * @param prefix 号码前缀
     * @param userNumber 用户号码
     * @return 返回完整的号码
     */
    public static String encodeNumberPrefix(String prefix, String number)
    {
        if (StringUtils.isEmpty(prefix))
        {
            return number;
        }
        
        if (number.toLowerCase(Locale.getDefault()).indexOf(prefix) == 0)
        {
            return number;
        }
        
        return prefix.concat(number);
    }
    
    /**
     * 从完整号码中获取用户号码
     * 
     * @param spNumber SP接入号
     * @param fullNumber 完整号码
     * @return 用户号码
     */
    public static String decodeNumberPrefix(String prefix, String number)
    {
        if (StringUtils.isEmpty(prefix) || StringUtils.isEmpty(number))
        {
            return number;
        }
        
        if (number.toLowerCase(Locale.getDefault()).indexOf(prefix.toLowerCase(Locale.getDefault())) == 0)
        {
            return number.substring(prefix.length());
        }
        
        return number;
    }
    
    /**
     * 将SP接入号与用户号码连接为完整的源号码
     * 
     * @param spNumber SP接入号
     * @param userNumber 用户号码
     * @return 返回完整的号码
     */
    public static String encodeSPNumber(String spNumber, String userNumber)
    {
        if (!StringUtils.isEmpty(spNumber) && !StringUtils.isEmpty(userNumber))
        {
            return spNumber.concat(userNumber);
        }
        else
        {
            if (!StringUtils.isEmpty(userNumber))
            {
                return userNumber;
            }
            else
            {
                return spNumber;
            }
        }
    }
    
    /**
     * 从完整号码中获取用户号码
     * 
     * @param spNumber SP接入号
     * @param fullNumber 完整号码
     * @return 用户号码
     */
    public static String decodeSPNumber(String spNumber, String fullNumber)
    {
        if (StringUtils.isEmpty(fullNumber) || StringUtils.isEmpty(spNumber))
        {
            return fullNumber;
        }
        else
        {
            if (fullNumber.indexOf(spNumber) == 0)
            {
                return fullNumber.substring(spNumber.length());
            }
            return fullNumber;
        }
    }
    
    public static String parserHexByte2Str(byte[] byteContent, int start, int end)
    {
        String hexStr = byte2HexStr(byteContent, start, end);
        String[] hexStrArray = hexStr.split(" ");
        StringBuilder result = new StringBuilder();
        for (String str : hexStrArray)
        {
            result.append(str.substring(2));
        }
        
        return result.toString();
    }
    
    //From TypeConvertTools
    public static String byte2HexStr(byte buffer[], int sPos, int ePos)
    {
        StringBuffer sb = new StringBuffer(((ePos - sPos) + 1) * 4);
        int mm = 0;
        for (int pos = sPos; pos <= ePos; pos++)
        {
            mm = buffer[pos] & 255;
            sb.append("0x");
            if (mm < 15)
                sb.append("0");
            sb.append(Integer.toHexString(mm));
            sb.append(" ");
        }
        
        return sb.toString();
    }
    
    public static byte[] hexStr2Byte(String buffer)
    {
        String arrs[] = buffer.trim().split(" ");
        byte mm = 0;
        byte ret[] = new byte[arrs.length];
        for (int index = 0; index < arrs.length; index++)
        {
            arrs[index] = arrs[index].trim();
            if ((arrs[index].charAt(1) < '0' || arrs[index].charAt(1) > '9') && arrs[index].length() > 2)
                arrs[index] = arrs[index].substring(2);
            mm = (byte)Integer.parseInt(arrs[index], 16);
            ret[index] = mm;
        }
        
        return ret;
    }    
    
    public static Map<String, String> getDataCodingCharsetMapping(String mappingStr)
    {
        Map<String, String> mapping = StringUtils.parseString(mappingStr, "\\|", ":");
        return mapping;
    }
}
