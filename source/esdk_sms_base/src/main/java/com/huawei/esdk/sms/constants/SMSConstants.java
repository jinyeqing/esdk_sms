package com.huawei.esdk.sms.constants;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.StringUtils;

public interface SMSConstants
{
    String MAS_NUMBER_PREFIX = "tel:";
    
    String ENCODE_GB2312 = "GB2312";
    
    String ENCODE_GBK = "GBK";
    
    String ENCODE_UTF8 = "UTF-8";
    
    /*
     * UCS2 (ISO/IEC-10646)
     */
    int DATA_ENCODING_UCS2 = 8;
    
    /*
     * UDHI Indicator (only relevant for MT short messages)
     */
    int ESM_CLASS_UDHI = 64;
    
    //处理成功
    int RESULT_SUCCESS = 1000;
    
    //处理失败
    int RESULT_FAILURE = 1001;

    //发送失败
    int SMS_SEND_ERROR = 3000;
    
    /**
     * 消息类型发送的短消息，下行短信
     */
    int SM_SUBMIT = 0;
    
    /**
     * 接收消息类型：短消息，上行短信
     */
    int SM_RECV_DELIVER = 1;
    
    /**
     * 接收消息类型：状态报告
     */
    int SM_RECV_REPORT = 2;
    
    /**
     * 国家码：中国
     */
    String COUNTRY_CODE = "86";
    
    String SP_NUMBER = StringUtils.avoidNull(ConfigManager.getInstance().getValue("smpp.sp-code"));
    
    String SP_NUMBER_CHINAMOBILE = StringUtils.avoidNull(ConfigManager.getInstance().getValue("cmpp.sp-code"));
    
    String SP_NUMBER_CHINAUNICOM = StringUtils.avoidNull(ConfigManager.getInstance().getValue("sgip.sp-code"));
    
    String SP_NUMBER_CHINATELECOM = StringUtils.avoidNull(ConfigManager.getInstance().getValue("smgp.sp-code"));
    
    String ESDK_SMS_NEADP_PLUGIN = "SMSPLUGIN";
}
