package com.huawei.esdk.tcp.base;

import java.io.IOException;
import java.net.Socket;

import com.huawei.esdk.tcp.base.pdu.PDU;

public interface ISession extends Runnable
{
    String getSessionId();
    
    void init(Socket socket);
    
    void stop();
    
    /**
     * Sends a PDU to the client.
     * @param pdu the PDU to send
     */
    void send(PDU pdu)
        throws IOException;
    
    /**
     * Implements the logic of receiving of the PDUs from client and passing
     * them to PDU processor. First starts receiver, then in cycle
     * receives PDUs and passes them to the proper PDU processor's
     * methods. After the function <code>stop</code> is called (externally)
     * stops the receiver, exits the PDU processor and closes the connection,
     * so no extry tidy-up routines are necessary.
     */
    void run();
    
    /**
     * Sets the timeout for receiving the complete message.
     * @param timeout the new timeout value
     */
    void setReceiveTimeout(long timeout);
    
    /**
     * Returns the current setting of receiving timeout.
     * @return the current timeout value
     */
    long getReceiveTimeout();
    
    /**
     * Returns the details about the account that is logged in to this session
     * @return An object representing the account. It is casted to the correct type by the implementation
     */
    Object getAccount();
    
    /**
     * Set details about the account that is logged in to this session 
     * @param account An object representing the account.
     */
    void setAccount(Object account);
    
    
    void setHasLogin(boolean loginFlag);
    
    boolean hasLogin();
    
    String getRemoteIP();
}
