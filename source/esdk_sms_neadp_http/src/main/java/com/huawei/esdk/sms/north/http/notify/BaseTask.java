package com.huawei.esdk.sms.north.http.notify;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.constants.ESDKConstant;
import com.huawei.esdk.platform.common.utils.ApplicationContextUtil;
import com.huawei.esdk.platform.commu.itf.IProtocolAdapterManager;
import com.huawei.esdk.platform.commu.itf.ISDKProtocolAdapter;
import com.huawei.esdk.platform.commu.itf.ISDKProtocolAdatperCustProvider;
import com.huawei.esdk.sms.device.SMSAdapterDevice;

public abstract class BaseTask
{
    protected SMSAdapterDevice smsAdapterDevice;
    
    protected ISDKProtocolAdapter sdkProtocolAdapter;
    
    private IProtocolAdapterManager protocolAdapterManager = ApplicationContextUtil.getBean("protocolAdapterManager");
    
    private String serverURL;
    
    public BaseTask()
    {
        init();
    }
    
    public void init()
    {
        serverURL = ConfigManager.getInstance().getValue("sms.http.gateway.url");
        
        if ("HTTP_JDK".equalsIgnoreCase(ConfigManager.getInstance().getValue("sms.http.impl.way")))
        {
            this.sdkProtocolAdapter =
                protocolAdapterManager.getProtocolInstanceByType(ESDKConstant.PROTOCOL_ADAPTER_TYPE_HTTP_JDK, serverURL);
        }
        else
        {
            this.sdkProtocolAdapter =
                protocolAdapterManager.getProtocolInstanceByType(ESDKConstant.PROTOCOL_ADAPTER_TYPE_HTTP, serverURL);
        }
        sdkProtocolAdapter.setSdkProtocolAdatperCustProvider(getSDKProtocolAdatperCustProvider());
    }
    
    protected abstract ISDKProtocolAdatperCustProvider getSDKProtocolAdatperCustProvider();
    
    
}
