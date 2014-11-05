package com.huawei.esdk.sms.openapi.http.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.utils.ApplicationContextUtil;
import com.huawei.esdk.platform.common.utils.ESDKIOUtils;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.sms.constants.SMSConstants;
import com.huawei.esdk.sms.openapi.http.service.OpenAPIHttpService;

public class SMSOpenAPIServlet extends HttpServlet
{
    private static final Logger LOGGER = Logger.getLogger(SMSOpenAPIServlet.class);
    
    private OpenAPIHttpService service = ApplicationContextUtil.getBean("openAPIHttpService");
    
    /**
     * UID
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        String userName = req.getParameter("userName");
        String password = req.getParameter("password");
        String encode = req.getParameter("encode");
        String to = req.getParameter("to");
        String content = req.getParameter("content");
        if (null != encode && !"UTF-8".equalsIgnoreCase(encode))
        {
            Map<String, String> params = StringUtils.parseString(req.getQueryString(), "&", "=");
            content = params.get("content");
        }
        
        LOGGER.info("A SMS send request arrived: to = " + to);        
        String resultCode = service.processSendSmsRequest(userName, password, encode, to, content);        
        LOGGER.info("The SMS processed result = " + resultCode);
        
        OutputStream os = null;
        try
        {
            os = resp.getOutputStream();
            os.write(resultCode.getBytes(SMSConstants.ENCODE_UTF8));
        }
        catch (IOException e)
        {
            LOGGER.error(e);
        }
        finally
        {
            ESDKIOUtils.closeOutputStream(os);
        }
    }  
}
