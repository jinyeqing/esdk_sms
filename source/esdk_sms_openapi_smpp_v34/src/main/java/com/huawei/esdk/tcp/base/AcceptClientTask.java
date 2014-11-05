package com.huawei.esdk.tcp.base;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class AcceptClientTask implements Runnable
{
    private static final Logger LOGGER = Logger.getLogger(AcceptClientTask.class);
    
    private ServerSocket serverSocket;
    
    private TCPServerEngine tcpServerEngine;
    
    private boolean runFlag;
    
    public AcceptClientTask(ServerSocket serverSocket, TCPServerEngine tcpServerEngine)
    {
        this.serverSocket = serverSocket;
        this.tcpServerEngine = tcpServerEngine;
        this.runFlag = true;
    }
    
    @Override
    public void run()
    {
        Socket socket;
        while (true && runFlag)
        {
            try
            {
                //To create a socket which used for accepting request from clients
                socket = serverSocket.accept();
                tcpServerEngine.addSocket(socket);
            }
            catch (IOException e)
            {
                LOGGER.error("", e);
            }
        }
    }
    
    public void stop()
    {
        runFlag = false;
    }
}
