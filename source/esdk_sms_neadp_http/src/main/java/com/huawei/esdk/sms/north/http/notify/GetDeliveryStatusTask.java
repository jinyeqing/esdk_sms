package com.huawei.esdk.sms.north.http.notify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.commu.itf.ISDKProtocolAdatperCustProvider;
import com.huawei.esdk.sms.device.SMSAdapterDevice;
import com.huawei.esdk.sms.model.SMSDeliveryStatus;
import com.huawei.esdk.sms.model.SMSStatus;
import com.huawei.esdk.sms.north.http.commu.SMSGetDeliveryStatusCustProvider;

public class GetDeliveryStatusTask extends BaseTask implements Runnable
{
    private static final Logger LOGGER = Logger.getLogger(GetSMSTask.class);
        
    public GetDeliveryStatusTask(SMSAdapterDevice smsAdapterDevice)
    {
        this.smsAdapterDevice = smsAdapterDevice;
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
            catch(Exception e)
            {
                LOGGER.error("", e);
            }
        }
    }

    private void processResult(List<Map<String, String>> param)
    {
        String content;
        for (Map<String, String> item : param)
        {
            content = item.get("@{Item_N}");
            
            smsAdapterDevice.onRecvReceipt(buildSMSDeliveryStatus(content));
        }
    }
    
    private SMSDeliveryStatus buildSMSDeliveryStatus(String content)
    {
        SMSDeliveryStatus smsDeliveryStatus = new SMSDeliveryStatus();
        List<SMSStatus> statusList = new ArrayList<SMSStatus>();
        smsDeliveryStatus.setStatusList(statusList);
        SMSStatus item = new SMSStatus();
        statusList.add(item);
        
        String[] tempArray = content.split(",");
        //日期
        //时间
        smsDeliveryStatus.setId(tempArray[2]);//信息编号
        item.setReservedField1(tempArray[3]);//*
        item.setStatus(tempArray[4]);//状态值
        item.setReservedField2(tempArray[5]);//详细错误原因
        
        return smsDeliveryStatus;
    }
    
    @Override
    protected ISDKProtocolAdatperCustProvider getSDKProtocolAdatperCustProvider()
    {
        return new SMSGetDeliveryStatusCustProvider();
    }
    
    
}
