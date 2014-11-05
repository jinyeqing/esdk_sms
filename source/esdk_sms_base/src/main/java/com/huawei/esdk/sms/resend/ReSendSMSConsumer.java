package com.huawei.esdk.sms.resend;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.NumberUtils;
import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.sms.utils.DateUtils;

/**
 * The consumer to consume the send failed SMSs from the specified queue.
 * 
 * @author z00209306 - Zhang Zhili
 * @since V100R002C01
 */
public class ReSendSMSConsumer implements Runnable
{
    /*
     * Debug logs for trouble shoot.
     */
    private static final Logger LOGGER = Logger.getLogger(ReSendSMSConsumer.class);
    
    /*
     * The queue to store the send failed SMS
     */
    private BlockingQueue<FailedSMSInfo> queue;
    
    private boolean runFlag;
    
    /**
     * The constructor.
     * 
     * @param queue The send failed SMS queue.
     * @param processor The processor to send the SMS.
     */
    public ReSendSMSConsumer(BlockingQueue<FailedSMSInfo> queue)
    {
        this.queue = queue;
        this.runFlag = true;
    }
    
    @Override
    public void run()
    {
        int reSendInterval = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("esdk.sms.resend.interval", "60"));
        FailedSMSInfo failedSMS = null;
        while (true && runFlag)
        {
            try
            {
                if (!queue.isEmpty())
                {
                    // If the queue is not empty, then get the first SMS to
                    // check
                    // whether it is meeting the re-send time interval.
                    failedSMS = queue.peek();
                    if (null != failedSMS)
                    {
                        if (reSendIntervalElapsed(failedSMS, reSendInterval))
                        {
                            failedSMS = queue.poll();
                            // To check the time period again in case of the
                            // first SMS
                            // is polled by another thread already.
                            if (reSendIntervalElapsed(failedSMS, reSendInterval))
                            {
                                processSendFailedSms(failedSMS);
                            }
                            else
                            {
                                // If the SMS doesn't meet the interval then put
                                // it back into the queue.
                                if (null != failedSMS)
                                {
                                    queue.put(failedSMS);
                                }
                            }
                        }
                        else
                        {
                            // Do the following sleep to avoid check the queue
                            // all the time as even the queue is not empty,
                            // but the SMS probably doesn't meet the re-send
                            // interval.
                            TimeUnit.SECONDS.sleep(1);
                        }
                    }
                }
                else
                {
                    // If the queue is empty, then wait to poll a SMS with 60
                    // seconds timeout
                    failedSMS = queue.poll(60, TimeUnit.SECONDS);
                    if (null != failedSMS)
                    {
                        if (reSendIntervalElapsed(failedSMS, reSendInterval))
                        {
                            processSendFailedSms(failedSMS);
                        }
                        else
                        {
                            // If the SMS doesn't meet the re-send interval then
                            // put it back into the queue.
                            queue.put(failedSMS);
                        }
                    }
                }
            }
            catch (InterruptedException e)
            {
                LOGGER.error("InterruptedException happened");
            }
        }
    }
    
    /**
     * To process the send failed SMS.
     * 
     * @param sms The SMS used for re-send
     */
    private void processSendFailedSms(FailedSMSInfo failedSMS)
    {
        try
        {
            if (null != failedSMS)
            {
                String identifier = failedSMS.getSmsAdapter().sendSMSMessage(failedSMS.getSmsMessage()).getValue();
                LOGGER.info("SMS message " + failedSMS.getSmsMessage().getId()
                    + " resend out successfully and the identifier is " + identifier);
            }
        }
        catch (Exception e)
        {
            failedSMS.getSmsMessage().setSendFailedCount(failedSMS.getSmsMessage().getSendFailedCount() + 1);
            LOGGER.info("SMS message " + failedSMS.getSmsMessage().getId() + " resend out failed", e);
            if (failedSMS.getSmsMessage().getSendFailedCount() >= NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("esdk.sms.resend.threshold",
                "3")))
            {
                LOGGER.error("SMS message[" + failedSMS.getSmsMessage().getSMS4Logging() + "] sent failed");
                PlatformSMSLogUtils.writeSendErrorLog("A SMS message[" + failedSMS.getSmsMessage().getSMS4Logging()
                    + "] sent failed");
            }
            else
            {
                try
                {
                    queue.put(failedSMS);
                }
                catch (InterruptedException e1)
                {
                    LOGGER.error("message id = " + failedSMS.getSmsMessage().getId(), e1);
                }
            }
        }
    }
    
    /**
     * To judge whether the time between now and SMS last sent failed point is
     * meet the specified time interval.
     * 
     * @param sms SMSMessage
     * @param reSendInterval The specified time interval, the UNIT is second
     * @return true if the time period is greater than or equal the specified
     *         time interval.
     */
    private boolean reSendIntervalElapsed(FailedSMSInfo failedSMS, int reSendInterval)
    {
        if (null == failedSMS)
        {
            return false;
        }
        
        long interval =
            (DateUtils.getCurrentDateTime().getTime() - failedSMS.getSmsMessage().getLastSendTime().getTime()) / 1000;
        if (interval >= reSendInterval)
        {
            return true;
        }
        
        return false;
    }
    
    public void stop()
    {
        this.runFlag = false;
    }
}