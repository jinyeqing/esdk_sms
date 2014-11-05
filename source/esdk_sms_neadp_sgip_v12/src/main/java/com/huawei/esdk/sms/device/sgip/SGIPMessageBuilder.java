package com.huawei.esdk.sms.device.sgip;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.NumberUtils;
import com.huawei.esdk.sms.constants.SMSConstants;
import com.huawei.esdk.sms.device.BaseMessageBuilder;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.utils.SMSUtils;
import com.huawei.smproxy.comm.sgip.message.SGIPSubmitMessage;

public class SGIPMessageBuilder extends BaseMessageBuilder
{
    
    public List<SGIPSubmitMessage> buildSGIPSubmitMessages(SMSMessage message, int normalSMMaxLength, int dataCoding)
        throws UnsupportedEncodingException
    {
        List<SGIPSubmitMessage> result = new ArrayList<SGIPSubmitMessage>(1);
        
        int registeredDelivery = message.isNeedReport() ? 1 : 0;
        byte[] contentByte = message.getContent().getBytes(getCharset(dataCoding));
        int smsLength = contentByte.length;
        if (smsLength > normalSMMaxLength)
        {
            //长短信
            dataCoding = DATA_ENCODING_UCS2;
            List<byte[]> smsList = splitLongMessage(message.getContent(), getCharset(dataCoding), normalSMMaxLength, 6);
            int index = 1;
            for (byte[] item : smsList)
            {
                result.add(doBuildSMGPSubmitMessage(message,
                    item,
                    dataCoding,
                    registeredDelivery,
                    1,
                    smsList.size(),
                    index++));
            }
        }
        else
        {
            result.add(doBuildSMGPSubmitMessage(message, contentByte, dataCoding, registeredDelivery, 0, 1, 1));
        }
        
        return result;
    }
    
    private SGIPSubmitMessage doBuildSMGPSubmitMessage(SMSMessage message, byte[] msgContentByte, int dataCoding,
        int registeredDelivery, int tpUdhi, int pkTotal, int pkNumber)
    {
        String chargeNumber = ConfigManager.getInstance().getValue("sgip.message.chargeNumber");
        
        if ("".equals(chargeNumber))
        {
            chargeNumber = SMSUtils.encodeNumberPrefix(SMSConstants.COUNTRY_CODE, message.getSrcId());
        }
        
        if (0 == registeredDelivery)
        {
            registeredDelivery = 2;
        }
        return new SGIPSubmitMessage(
            SMSConstants.SP_NUMBER_CHINAUNICOM,
            chargeNumber, // 付费号码，字符，手机号码前加“86”国别标志 , 如果为全零字符串“000000000000000000000”，表示该条短消息产生的费用由SP支付
            encodeDestIds(message.getDestId()),
            ConfigManager.getInstance().getValue("sgip.sp-id"),//企业代码，取值范围0-99999
            ConfigManager.getInstance().getValue("sgip.message.servicetype"),//业务代码，由SP定义
            Integer.valueOf(ConfigManager.getInstance().getValue("sgip.message.feetype")),
            ConfigManager.getInstance().getValue("sgip.message.feevalue"), ConfigManager.getInstance().getValue("sgip.message.givenvalue"),
            Integer.valueOf(ConfigManager.getInstance().getValue("sgip.message.agentflag")),
            NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("sgip.message.morelatetomtflag")),// MorelatetoMTFlag,
            Integer.valueOf(ConfigManager.getInstance().getValue("sgip.message.priority")), null, // 短消息寿命的终止时间
            null, // 短消息定时发送的时间
            registeredDelivery, // 1 需要；2 不需要
            NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("sgip.message.tppid")),// TP_pid
            tpUdhi, // TP_udhi
            dataCoding, // 短消息的编码格式。   0：纯ASCII字符串   8：UCS2编码   15: GBK编码
            NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("sgip.message.messagetype")),// 信息类型： 0-短消息信息
            msgContentByte.length, 
            msgContentByte,
            "");
    }
    
    private String[] encodeDestIds(String[] ids)
    {
        if (null == ids)
        {
            return new String[0];
        }
        
        for (int i = 0; i < ids.length; i++)
        {
            ids[i] = SMSUtils.encodeNumberPrefix(SMSConstants.COUNTRY_CODE, ids[i]);
        }
        
        return ids;        
    }
    
    protected String getMappingProp()
    {
        return "sgip.data-coding-charset-mapping";
    }

    @Override
    protected String getSpNumber()
    {
        return SMSConstants.SP_NUMBER_CHINAUNICOM;
    }
    
    protected String getProtocolType()
    {
        return "sgip";
    }
}
