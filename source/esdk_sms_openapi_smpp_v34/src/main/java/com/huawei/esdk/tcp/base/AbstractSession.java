package com.huawei.esdk.tcp.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.huawei.esdk.tcp.base.pdu.PDU;

public abstract class AbstractSession implements ISession
{
    private static final Logger LOGGER = Logger.getLogger(AbstractSession.class);
    
    private static final int RECEIVER_DATA_TIMEOUT = 151000;
    
    private static final int RECEIVER_DATA_INTERVAL = 500;
    
    protected String remoteIP;
    
    private String sessionId;
    
    private long receiveTimeout;
    
    private boolean loginFlag;
    
    private boolean keepReceiving;
    
    protected BlockingQueue<PDU> incomingMessages;
    
    private ByteDataProcessor byteDataProcessor;
    
    private TCPServerEngine tcpServerEngine;
    
    private Socket socket;
    
    private InputStream inputStream;
    
    private OutputStream outputStream;
    
    public AbstractSession(ByteDataProcessor byteDataProcessor, TCPServerEngine tcpServerEngine)
    {
        this.receiveTimeout = RECEIVER_DATA_TIMEOUT;
        this.sessionId = UUID.randomUUID().toString();
        this.tcpServerEngine = tcpServerEngine;
        this.byteDataProcessor = byteDataProcessor;
        incomingMessages = new ArrayBlockingQueue<PDU>(5000);
    }
    
    public String getSessionId()
    {
        return this.sessionId;
    }
    
    public void init(Socket socket)
    {
        this.socket = socket;
        remoteIP = this.socket.getInetAddress().getHostAddress();
        keepReceiving = true;
        try
        {
            inputStream = this.socket.getInputStream();
            outputStream = this.socket.getOutputStream();
        }
        catch (IOException e)
        {
            LOGGER.error("", e);
        }
        
        new Thread(new ProcessInputMsgTask()).start();
    }
    
    @Override
    public void send(PDU pdu)
        throws IOException
    {
        outputStream.write(pdu.getByteData());
    }
    
    @Override
    public void run()
    {
        List<PDU> pduList = null;
        byte[] temp = new byte[1024 * 4];
        byte[] message;
        int count = 0;
        int byteValue = 1;
        int bytesToRead;
        int timeoutCounter = 0;
        while (keepReceiving)
        {
            count = 0;
            byteValue = 1;
            try
            {
                bytesToRead = inputStream.available();
                while (bytesToRead > 0)
                {
                    byteValue = inputStream.read();
                    if (byteValue > -1)
                    {
                        temp[count++] = (byte)byteValue;
                    }
                    bytesToRead--;
                }
                if (count > 0)
                {
                    message = new byte[count];
                    System.arraycopy(temp, 0, message, 0, count);
                    pduList = byteDataProcessor.processByteData(message);
                    timeoutCounter = 0;
                }
                else
                {
                    timeoutCounter++;
                    if (timeoutCounter > ((int) receiveTimeout / RECEIVER_DATA_INTERVAL))
                    {
                        stop();
                    }
                }
                
                if (null != pduList)
                {
                    for (PDU pdu : pduList)
                    {
                        if (null != pdu)
                        {
                            incomingMessages.put(pdu);
                        }
                    }
                    pduList.clear();
                }
                
                TimeUnit.MILLISECONDS.sleep(RECEIVER_DATA_INTERVAL);
            }
            catch (Exception e)
            {
                LOGGER.error("", e);
            }
        }
    }
    
    @Override
    public void setReceiveTimeout(long timeout)
    {
        this.receiveTimeout = timeout;
    }
    
    @Override
    public long getReceiveTimeout()
    {
        return this.receiveTimeout;
    }
    
    public void setHasLogin(boolean loginFlag)
    {
        this.loginFlag = loginFlag;
    }
    
    public boolean hasLogin()
    {
        return this.loginFlag;
    }
    
    protected abstract void processPDU(PDU pdu);
    
    private class ProcessInputMsgTask implements Runnable
    {
        @Override
        public void run()
        {
            PDU pdu;
            while (true && keepReceiving)
            {
                try
                {
                    pdu = incomingMessages.poll(10, TimeUnit.SECONDS);
                    if (null != pdu)
                    {
                        processPDU(pdu);
                    }
                }
                catch (Exception e)
                {
                    LOGGER.error("", e);
                }
            }
        }
    }
    
    public void stop()
    {
        keepReceiving = false;
        
        try
        {
            if (null != inputStream)
            {
                inputStream.close();
            }
            
            if (null != outputStream)
            {
                outputStream.close();
            }
            
            if (null != socket)
            {
                this.socket.close();
            }
        }
        catch (IOException e)
        {
            LOGGER.error("", e);
        }
        
        tcpServerEngine.removeSession(this);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (null != obj && obj instanceof AbstractSession)
        {
            if (sessionId.equals(((AbstractSession)obj).getSessionId()))
            {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return sessionId.hashCode();
    }
    
    public String getRemoteIP()
    {
        return this.remoteIP;
    }
}
