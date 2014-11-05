package com.huawei.esdk.platform.common.utils;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.sms.countsms.SMSCounter;

/**
 * This package and the class here is for logging the information into different
 * log files. If put this class into the package
 * com.huawei.esdk.platform.sms.utils, it's not easy to write different logs
 * into different files.
 * 
 * @author z00209306 Zhang Zhili
 */
public abstract class PlatformSMSLogUtils
{
    /*
     * Logger for logging SMS which sent out failed.
     */
    private static final Logger SMS_SEND_ERROR_LOGGER = Logger.getLogger("SMSSendError");

    /*
     * Logger for logging incoming SMSs and output SMSs.
     */
    private static final Logger SMS_IN_OUT_LOGGER = Logger.getLogger("SMSInout");
    
    
    private static String way;

    static
    {
        way = ConfigManager.getInstance().getValue("message.write.way");
    }

    /**
     * Write the Send Failed SMS log.
     * 
     * @param logContent The send failed SMS info.
     */
    public static void writeSendErrorLog(String logContent)
    {
        if ("0".equals(way))
        {
            SMS_SEND_ERROR_LOGGER.error(logContent);
        }
        else
        {
            SMSCounter.plusFailure();
        }
    }

    /**
     * Write the eSDK SMS adapter received and delivered SMS log.
     * 
     * @param logContent The incoming/outgoing SMS info.
     */
    public static void writeInOutSmsLog(String logContent)
    {
        if ("0".equals(way))
        {
            SMS_IN_OUT_LOGGER.info(logContent);
        }
        else
        {
            SMSCounter.plusSuccess();
        }
    }
}
