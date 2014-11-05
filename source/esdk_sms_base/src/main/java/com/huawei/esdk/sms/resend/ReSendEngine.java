package com.huawei.esdk.sms.resend;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.NumberUtils;

/**
 * The SMS re-send Engine for re-sending the sent failed SMS firstly.
 * 
 * @author z00209306 - Zhang Zhili
 * @since V100R002C01
 */
public class ReSendEngine
{
    private static final Logger LOGGER = Logger.getLogger(ReSendEngine.class);

    /*
     * Re-sent queue for Resent SMS
     */
    private BlockingQueue<FailedSMSInfo> smsResendQueue;
    
    private List<ReSendSMSConsumer> consumers = new ArrayList<ReSendSMSConsumer>();
    
    private ExecutorService es = Executors.newCachedThreadPool(new ThreadFactory()
                                    {
                                        public Thread newThread(Runnable r)
                                        {
                                            Thread t = new Thread(r);
                                            t.setDaemon(true);
                                            return t;
                                        }
                                    }
                                );
    
    /**
     * To initiate the Re-send SMS engine.
     */
    public void init()
    {
        int capablity = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("sms.resend.queue.capability", "5000"));
        int reSendThreads = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("esdk.sms.resend.threads", "1"));
        smsResendQueue =  new ArrayBlockingQueue<FailedSMSInfo>(capablity, true);
        
        for (int i = 0; i < reSendThreads; i++)
        {
            consumers.add(new ReSendSMSConsumer(smsResendQueue));
            es.execute(consumers.get(i));
        }
    }
    
    /**
     * To queue in the send failed SMS.
     * 
     * @param sms The send failed SMSMessage
     */
    public void intoQueue(FailedSMSInfo sms)
    {
        LOGGER.debug("A send failed SMS (message id = " + sms.getSmsMessage().getId() + ") is input int re-send queue");
        try
        {
            smsResendQueue.put(sms);
        }
        catch(InterruptedException e)
        {
            LOGGER.error("", e);
        }
    }
    
    public void destroy()
    {
        for (ReSendSMSConsumer consumer : consumers)
        {
            consumer.stop();
        }
        es.shutdownNow();
    }
}