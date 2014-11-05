package com.huawei.esdk.sms.device.cmpp;

import java.lang.reflect.Proxy;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.exception.SDKException;
import com.huawei.esdk.platform.nemgr.base.DeviceConnectionBase;
import com.huawei.esdk.platform.nemgr.base.MultiConnDeviceBase;

public class CMPPConnection extends DeviceConnectionBase
{
    private static final Logger LOGGER = Logger.getLogger(CMPPConnection.class);
    
    private MultiConnDeviceBase device;
    
    protected CMPPConnection(String user, String pwd, MultiConnDeviceBase serviceProxy)
    {
        super(user, pwd);
        this.device = serviceProxy;
    }
    
    @Override
    public Object getServiceProxy(Class<?>[] itfs)
        throws SDKException
    {
        if (itfs.length == 1)
        {
            if (itfs[0].isInstance(device))
            {
                return device;
            }
            return device.getService(itfs[0]);
        }
        else
        {
            LOGGER.debug("Intefaces number is not 1");
            return Proxy.newProxyInstance(this.getClass().getClassLoader(), itfs, device.getService(itfs));
        }
    }
    
    @Override
    public boolean doHeartbeat(String connId)
    {
        return true;
    }
    
    @Override
    public boolean initConn(String connId)
    {
        return true;
    }
    
    @Override
    public void destroyConn(String connId)
    {
    }
}
