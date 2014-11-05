package com.huawei.esdk.sms.interceptor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.MaskUtils;

public class SMSLoggingInInterceptor extends LoggingInInterceptor
{
    private static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
            .getLogger(SMSLoggingInInterceptor.class);
    
    private static String SENSITIVE_WORDS;

    static
    {
        SENSITIVE_WORDS = ConfigManager.getInstance().getValue("sms.soap.sensitive.words");
    }
    
    @Override
    protected void logging(Logger logger, Message message) throws Fault
    {
        if (message.containsKey(LoggingMessage.ID_KEY))
        {
            return;
        }
        String id = (String) message.getExchange().get(LoggingMessage.ID_KEY);
        if (id == null)
        {
            id = LoggingMessage.nextId();
            message.getExchange().put(LoggingMessage.ID_KEY, id);
        }
        message.put(LoggingMessage.ID_KEY, id);
        final LoggingMessage buffer = new LoggingMessage(
                "Inbound Message\n----------------------------", id);

        Integer responseCode = (Integer) message.get(Message.RESPONSE_CODE);
        if (responseCode != null)
        {
            buffer.getResponseCode().append(responseCode);
        }

        String encoding = (String) message.get(Message.ENCODING);

        if (encoding != null)
        {
            buffer.getEncoding().append(encoding);
        }
        String httpMethod = (String) message.get(Message.HTTP_REQUEST_METHOD);
        if (httpMethod != null)
        {
            buffer.getHttpMethod().append(httpMethod);
        }
        String ct = (String) message.get(Message.CONTENT_TYPE);
        if (ct != null)
        {
            buffer.getContentType().append(ct);
        }
        @SuppressWarnings("unchecked")
        Map<String, List<String>> headers = (Map<String, List<String>>) message
                .get(Message.PROTOCOL_HEADERS);

        if (headers != null)
        {
            buffer.getHeader().append(headers);
        }
        String uri = (String) message.get(Message.REQUEST_URL);
        if (uri != null)
        {
            buffer.getAddress().append(uri);
            String query = (String) message.get(Message.QUERY_STRING);
            if (query != null)
            {
                buffer.getAddress().append("?").append(query);
            }
        }

        InputStream is = message.getContent(InputStream.class);
        if (is != null)
        {
            CachedOutputStream bos = new CachedOutputStream();
            try
            {
                IOUtils.copy(is, bos);

                bos.flush();

                message.setContent(InputStream.class, bos.getInputStream());
                File tempFile = bos.getTempFile();
                if (tempFile != null)
                {
                    // large thing on disk...
                    buffer.getMessage().append(
                            "\nMessage (saved to tmp file):\n");
                    buffer.getMessage().append(
                            "Filename: " + tempFile.getAbsolutePath()
                                    + "\n");
                }
                if (bos.size() > limit)
                {
                    buffer.getMessage().append(
                            "(message truncated to " + limit + " bytes)\n");
                }
                writePayload(buffer.getPayload(), bos, encoding, ct);
            }
            catch (Exception e)
            {
                throw new Fault(e);
            }
            finally
            {
                try
                {
                    if (null != is)
                    {
                        is.close();
                    }
                }
                catch (IOException e)
                {
                    LOGGER.error("handleMessage os close error", e);
                }
                try
                {
                    bos.close();
                }
                catch (IOException e)
                {
                    LOGGER.error("handleMessage os close error", e);
                }
            }
        }
        String strBuffer = buffer.toString();
        log(logger, MaskUtils.mask(strBuffer, SENSITIVE_WORDS));
    }
}
