package com.huawei.esdk.sms.north.http.notify;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.platform.commu.itf.ISDKProtocolAdatperCustProvider;
import com.huawei.esdk.sms.device.SMSAdapterDevice;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.north.http.commu.SMSGetSMSCustProvider;
import com.huawei.esdk.sms.utils.SMSUtils;

public class GetSMSTask extends BaseTask implements Runnable
{
    private static final Logger LOGGER = Logger.getLogger(GetSMSTask.class);
    
    private String[] prefixes;
    
    public GetSMSTask(SMSAdapterDevice smsAdapterDevice)
    {
        this.smsAdapterDevice = smsAdapterDevice;
        String prefixConf = StringUtils.avoidNull(ConfigManager.getInstance().getValue("sms.http.gateway.get.sms.number.prefix"));
        prefixes = prefixConf.split(",");
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void run()
    {
        List<Map<String, String>> result;
        while (true)
        {
            try
            {
                TimeUnit.SECONDS.sleep(1);
                
                result =
                    (List<Map<String, String>>)sdkProtocolAdapter.syncSendMessage(null,
                    		ConfigManager.getInstance().getValue("sms.http.gateway.xml.request.method"),
                        "java.util.List");
                
                processResult(result);
            }
            catch (Exception e)
            {
                LOGGER.error("", e);
            }
        }
    }
    
    private void processResult(List<Map<String, String>> param)
    {
        String content;
        SMSMessage smsMessage;
        for (Map<String, String> item : param)
        {
            content = item.get("@{Item_N}");
            smsMessage = buildSMSMessage(content);
            PlatformSMSLogUtils.writeInOutSmsLog("A SMS [" + smsMessage.getSMS4Logging()
                + "] is arrived from HTTP Gateway");
            smsAdapterDevice.onRecvIncomeSMS(smsMessage);
        }
    }
    
    private SMSMessage buildSMSMessage(String content)
    {
        SMSMessage smsMessage = new SMSMessage();
        
        String[] tempArray = content.split(",");
        //日期
        //时间
        smsMessage.setSrcId(tempArray[2]);//上行源号码
        //Need decode number base on 上行目标通道号
        smsMessage.setDestId(new String[]{getPureNumber(tempArray[3])});//上行目标通道号
        smsMessage.setReserve1(tempArray[4]);//*
        smsMessage.setContent(tempArray[5]);//信息内容
        
        return smsMessage;
    }
    
    private String getPureNumber(String number)
    {
        for (String item : prefixes)
        {
            if (number.startsWith(item))
            {
                return SMSUtils.decodeNumberPrefix(item, number);
            }
        }
        
        return number;
    }
    
    @Override
    protected ISDKProtocolAdatperCustProvider getSDKProtocolAdatperCustProvider()
    {
        return new SMSGetSMSCustProvider();
    }
}