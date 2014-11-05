package com.huawei.esdk.tcp.base;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.NumberUtils;
import com.huawei.esdk.platform.common.utils.StringUtils;

public class TCPServerEngine
{
    private static final Logger LOGGER = Logger.getLogger(TCPServerEngine.class);
    
    private ExecutorService executor;
    
    private AcceptClientTask clientTask;
    
    private ServerSocket serverService;
    
    private List<ISession> sessions;
    
    private SessionProducer sessionProducer;
    
    /**
     * To do the initialization things for a instance. 
     *
     * @since eSDK Solutions Platform V100R003C00
     */
    public void init()
    {
        String ip = ConfigManager.getInstance().getValue("esdk.smpp.server.ip");
        if (StringUtils.isEmpty(ip))
        {
            return;
        }
    	int portNumber = NumberUtils.parseIntValue(ConfigManager.getInstance().getValue("esdk.smpp.server.port", "7890"));
        sessions = new ArrayList<ISession>();
        executor = Executors.newCachedThreadPool(new ThreadFactory()
                {
                    public Thread newThread(Runnable r)
                    {
                        Thread t = new Thread(r);
                        t.setDaemon(true);
                        return t;
                    }
                }
            );
        
        //To create a ServerSocket which is used by server application
        //to obtain a port and listen for client requests
        try
        {
            if (StringUtils.isNotEmpty(ip))
            {
                InetAddress inetAddress = InetAddress.getByName(ip);
                serverService = new ServerSocket(portNumber, 0, inetAddress);
            }
            else
            {
                serverService = new ServerSocket(portNumber);
            }
        }
        catch (IOException e)
        {
            LOGGER.error("", e);
        }
        
        //To start a separated thread to accept client connection(s).
        if (null != serverService)
        {
            clientTask = new AcceptClientTask(serverService, this);
            executor.execute(clientTask);
        }
    }
    
    public void addSocket(Socket socket)
    {
        ISession session = sessionProducer.buildSession(this);
        session.init(socket);
        sessions.add(session);
        LOGGER.debug("The total session now is " + sessions.size());
        executor.execute(session);
    }
    
    public void removeSession(ISession session)
    {
        sessions.remove(session);
        LOGGER.debug("The total session after removed is " + sessions.size());
    }
    
    public void destroy()
    {
        if (null != clientTask)
        {
            clientTask.stop();
        }
        try
        {
            if (null != serverService)
            {
                serverService.close();
            }
        }
        catch (IOException e)
        {
            LOGGER.error("", e);
        }
        executor.shutdownNow();
    }

    public List<ISession> getSessions()
    {
        return sessions;
    }

    public void setSessions(List<ISession> sessions)
    {
        this.sessions = sessions;
    }

    public SessionProducer getSessionProducer()
    {
        return sessionProducer;
    }

    public void setSessionProducer(SessionProducer sessionProducer)
    {
        this.sessionProducer = sessionProducer;
    }
}
