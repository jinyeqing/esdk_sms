package com.huawei.esdk.sms.device.smgp;

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
import com.huawei.smproxy.comm.smgp.message.SMGPSubmitMessage;

public class SMGPMessageBuilder extends BaseMessageBuilder
{
    public List<SMGPSubmitMessage> buildSMGPSubmitMessages(SMSMessage message, int normalSMMaxLength, int dataCoding)
        throws UnsupportedEncodingException
    {
        List<SMGPSubmitMessage> result = new ArrayList<SMGPSubmitMessage>(1);
        
        int registeredDelivery = message.isNeedReport() ? 1 : 0;
        int smsLength = message.getContent().getBytes(getCharset(dataCoding)).length;
        if (smsLength > normalSMMaxLength)
        {
            dataCoding = DATA_ENCODING_UCS2;
            //长短信
            List<byte[]> smsList = splitLongMessage(message.getContent(), getCharset(dataCoding), normalSMMaxLength, 6);
            int index = 1;
            for (byte[] item : smsList)
            {
                result.add(doBuildSMGPSubmitMessage(message,
                    new String(item, getCharset(dataCoding)),
                    dataCoding,
                    registeredDelivery,
                    1,
                    smsList.size(),
                    index++));
            }
        }
        else
        {
            result.add(doBuildSMGPSubmitMessage(message,
                message.getContent(),
                dataCoding,
                registeredDelivery,
                0,
                1,
                1));
        }
        
        return result;
    }
    
    private SMGPSubmitMessage doBuildSMGPSubmitMessage(SMSMessage message,
        String messageContent, int dataCoding, int registeredDelivery, int tpUdhi, int pkTotal, int pkNumber)
    {
        return new SMGPSubmitMessage(
            6,//int msgType:0-MO,6-MT,7-point-to-point
            registeredDelivery,// int needReport
            NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("smgp.message.priority")),// int priority:0~3
            ConfigManager.getInstance().getValue("smgp.message.service.id"),// String serviceId:业务代码
            ConfigManager.getInstance().getValue("smgp.message.fee.type"),// String feeType: 00~03
            ConfigManager.getInstance().getValue("smgp.message.fee.code"),// String feeCode
            ConfigManager.getInstance().getValue("smgp.message.fixed.fee"),// String fixedFee
            dataCoding,// int dataCoding
            DateUtils.getCurrentDateTime(),// Date validTime
            DateUtils.getCurrentDateTime(),// Date atTime
            encodeNumber(message.getSrcId()),// String srcTermId
            message.getSrcId(),// String chargeTermId
            message.getDestId(),// String destTermId[]
            messageContent,// String msgContent,
            StringUtils.avoidNull(ConfigManager.getInstance().getValue("smgp.message.reserve")),//String reserve
            tpUdhi,//tP_udhi
            NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("smgp.message.charge.user.type")),//ChargeUserType
            NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("smgp.message.tp.pid")),//tp_pId
            ConfigManager.getInstance().getValue("smgp.message.link.id"),//linkId
            ConfigManager.getInstance().getValue("smgp.sp-id"),//msgSrc
            ConfigManager.getInstance().getValue("smgp.message.mservice.id")//mServiceId
        );
    }
    
    @Override
    protected String getMappingProp()
    {
        return "smgp.data-coding-charset-mapping";
    }

    @Override
    protected String getSpNumber()
    {
        return SMSConstants.SP_NUMBER_CHINATELECOM;
    }
    
    protected String getProtocolType()
    {
        return "smgp";
    }
}
