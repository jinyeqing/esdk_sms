package com.huawei.esdk.sms.north.http.commu;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.bean.aa.AccountInfo;
import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.platform.commu.itf.ISDKProtocolAdatperCustProvider;
import com.huawei.esdk.sms.model.SMSMessage;

public class SMSHttpProtocolAdatperCustProvider extends SMSBaseProtocolAdatperCustProvider implements
    ISDKProtocolAdatperCustProvider
{
    private static final Logger LOGGER = Logger.getLogger(SMSHttpProtocolAdatperCustProvider.class);
    
    private String prefixNum;
    
    public SMSHttpProtocolAdatperCustProvider()
    {
    }
    
    protected String getSOAPActionPropName()
    {
        return "sms.http.gateway.send.soapaction";
    }
    
    public String getReqTemplateFileName()
    {
        return "sms_http_request.xml";
    }
    
    public String getResTemplateFileName()
    {
        return "sms_http_response.xml";
    }
    
    @Override
    public String getContent4Sending(Object reqMessage)
    {
        LOGGER.debug("SMS Request template is ");
        SMSMessage smsMessage = (SMSMessage)reqMessage;
        if (null == prefixNum)
        {
            prefixNum = StringUtils.avoidNull(ConfigManager.getInstance().getValue("sms.http.gateway.send.sms.number.prefix"));
        }
        
        if (StringUtils.isNotEmpty(prefixNum))
        {
            formatDestNumbers(prefixNum, smsMessage);
        }
        
        return fillSendMessage(StringUtils.avoidNull(getSMSSeq()),
            StringUtils.avoidNull(smsMessage.getSrcId()),
            StringUtils.avoidNull(smsMessage.getDestIdAsString()),
            (smsMessage.getDestId() == null ? 0 : smsMessage.getDestId().length),
            encode(StringUtils.avoidNull(smsMessage.getContent())));
    }
    
    private void formatDestNumbers(String prefixNum, SMSMessage smsMessage)
    {
        if (null == smsMessage.getDestId())
        {
            return ;
        }
        
        int index = 0;
        for (String num : smsMessage.getDestId())
        {
            if (!num.startsWith(prefixNum))
            {
                if (num.startsWith("+" + prefixNum))
                {
                    smsMessage.getDestId()[index] = num.substring(1);
                }
                else if (("+" + num).startsWith(prefixNum))
                {
                    smsMessage.getDestId()[index] = "+" + num;
                }
                else if (num.startsWith("00" + prefixNum))
                {
                    smsMessage.getDestId()[index] = num.substring(2);
                }
                else
                {
                    smsMessage.getDestId()[index] = prefixNum + num;
                }
            }
            index++;
        }
    }
    
    private String encode(String content)
    {
        return content;
    }
    
    private String getSMSSeq()
    {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    @Override
    public AccountInfo getProtocolAuthInfo()
    {
        return null;
    }
}
