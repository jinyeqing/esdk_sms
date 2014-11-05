package com.huawei.esdk.sms.device.cmpp;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.NumberUtils;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.sms.constants.SMSConstants;
import com.huawei.esdk.sms.device.BaseMessageBuilder;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.utils.DateUtils;
import com.huawei.smproxy.comm.cmpp30.message.CMPP30SubmitMessage;

public class CMPPMessageBuilder extends BaseMessageBuilder
{
    public List<CMPP30SubmitMessage> buildCMPP30SubmitMessages(SMSMessage message, int normalSMMaxLength, int dataCoding)
        throws UnsupportedEncodingException
    {
        List<CMPP30SubmitMessage> result = new ArrayList<CMPP30SubmitMessage>(1);
        
        int registeredDelivery = message.isNeedReport() ? 1 : 0;
        int smsLength = message.getContent().getBytes(getCharset(dataCoding)).length;
        
        byte[] msgContent = message.getContent().getBytes((getCharset(dataCoding)));
        
        if (smsLength > normalSMMaxLength)
        {
            dataCoding = DATA_ENCODING_UCS2;
            List<byte[]> smsList = splitLongMessage(message.getContent(), getCharset(dataCoding), normalSMMaxLength, 6);
            int index = 1;
            for (byte[] item : smsList)
            {
                result.add(doBuildCMPP30SubmitMessage(message,
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
            result.add(doBuildCMPP30SubmitMessage(message,
                msgContent,
                dataCoding,
                registeredDelivery,
                0,
                1,
                1));
        }
        
        return result;
    }
    
    private CMPP30SubmitMessage doBuildCMPP30SubmitMessage(SMSMessage message,
        byte[] msgContent, int dataCoding, int registeredDelivery, int tpUdhi, int pkTotal, int pkNumber)
    {
        String terminalId = "";
        if ("3".equalsIgnoreCase(ConfigManager.getInstance().getValue("cmpp.message.fee.user.type")))
        {
            terminalId = message.getSrcId();
        }
        
        return new CMPP30SubmitMessage(pkTotal,//int pk_Total
            pkNumber,//int pk_Number
            registeredDelivery,//int registered_Delivery:是否需要返回状态报告
            NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("cmpp.message.content.msg.level")),//int msg_Level
            StringUtils.avoidNull(ConfigManager.getInstance().getValue("cmpp.message.service.id")),//String service_Id
            NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("cmpp.message.fee.user.type")),//int fee_UserType
            terminalId,//String fee_Terminal_Id
            NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("cmpp.message.fee.terminal.type")),//int fee_Terminal_Type
            		NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("cmpp.message.tp.pid")),//int tp_Pid
            tpUdhi,//SmsAdapterPropertiesUtils.getIntValue("cmpp.message.tp.udhi"),//int tp_Udhi
            dataCoding,//SmsAdapterPropertiesUtils.getIntValue("cmpp.message.msg.fmt"),//int msg_Fmt
            ConfigManager.getInstance().getValue("cmpp.sp-id", ""),//String msg_Src:sp_id
            StringUtils.avoidNull(ConfigManager.getInstance().getValue("cmpp.message.fee.type")),//String fee_Type
            StringUtils.avoidNull(ConfigManager.getInstance().getValue("cmpp.message.fee.code")),//String fee_Code
            DateUtils.getCurrentDateTime(),//Date valid_Time,
            DateUtils.getCurrentDateTime(),//Date at_Time,
            encodeNumber(message.getSrcId()),//String src_Terminal_Id,
            message.getDestId(),//String dest_Terminal_Id[],
            NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("cmpp.message.dest.terminal.type")),//int dest_Terminal_Type,
            msgContent,//byte msg_Content[],
            StringUtils.avoidNull(ConfigManager.getInstance().getValue("cmpp.message.link.id"))//String LinkID
        );
    }
    
    @Override
    protected String getMappingProp()
    {
        return "cmpp.data-coding-charset-mapping";
    }

    @Override
    protected String getSpNumber()
    {
        return SMSConstants.SP_NUMBER_CHINAMOBILE;
    }
    
    protected String getProtocolType()
    {
        return "cmpp";
    }
}
