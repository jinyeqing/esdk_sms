package com.huawei.esdk.sms.countsms;

//import java.util.Date;

import org.apache.log4j.Logger;

//import com.huawei.esdk.platform.common.config.ConfigManager;
//import com.huawei.esdk.sms.utils.DateUtils;

public class SMSCounter
{
    protected static final Logger LOGGER = Logger.getLogger("SMSCount");
    
//    private String lastWriteTime;
    
    private static long successCount = 0;
    
    private static long failureCount = 0;
    
    private static Object locker = new Object();
    
//    public void writeCount()
//    {
//        String nowWriteTime = DateUtils.formatDate(new Date(), DateUtils.DT_FT_YYYYMMDDHH);
//        String way = ConfigManager.getInstance().getValue("message.write.way");
//        
//        if ("1".equals(way))
//        {
//            synchronized (locker)
//            {
//                if (null == lastWriteTime)
//                {
//                    LOGGER.info("from startup to " + nowWriteTime + " ,there are " + successCount
//                        + " success messages!");
//                }
//                else
//                {
//                    LOGGER.info("from " + lastWriteTime + " to " + nowWriteTime + " ,there are " + successCount
//                        + " success messages!");
//                }
//                successCount = 0;
//            }
//            
//            synchronized (locker)
//            {
//                if (null == lastWriteTime)
//                {
//                    LOGGER.info("from startup to " + nowWriteTime + " ,there are " + failureCount
//                        + " failure messages!");
//                }
//                else
//                {
//                    LOGGER.info("from " + lastWriteTime + " to " + nowWriteTime + " ,there are " + failureCount
//                        + " failure messages!");
//                }
//                failureCount = 0;
//                
//                lastWriteTime = nowWriteTime;
//            }
//        }
//    }
    
    public static void plusSuccess()
    {
        synchronized (locker)
        {
            successCount++;
        }
    }
    
    public static void plusFailure()
    {
        synchronized (locker)
        {
            failureCount++;
        }
    }
    
    public static long getSuccessCount()
    {
        return successCount;
    }

    public static long getFailureCount()
    {
        return failureCount;
    }
}
