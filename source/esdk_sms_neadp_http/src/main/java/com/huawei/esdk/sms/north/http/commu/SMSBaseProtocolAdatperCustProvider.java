package com.huawei.esdk.sms.north.http.commu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.AESCbc128Utils;
import com.huawei.esdk.platform.common.utils.ESDKIOUtils;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.platform.commu.itf.ISDKProtocolAdatperCustProvider;
import com.huawei.esdk.platform.exception.ProtocolAdapterException;
import com.huawei.esdk.sms.north.http.bean.PlaceHolderBean;
import com.huawei.esdk.sms.north.http.common.IXMLProcessor;
import com.huawei.esdk.sms.north.http.common.ResponseTemplateXMLProcessor;
import com.huawei.esdk.sms.north.http.common.ResponseXMLProcessor;
import com.huawei.esdk.sms.north.http.common.ResponseXMLStringProcessor;

public abstract class SMSBaseProtocolAdatperCustProvider implements ISDKProtocolAdatperCustProvider
{
    private static final Logger LOGGER = Logger.getLogger(SMSBaseProtocolAdatperCustProvider.class);
    
    private String soapAction;
    
    protected boolean contentNeedEscape;
    
    protected boolean resContentNeedUnescape;
    
    protected String resContentParsingWay;
    
    protected String userId;
    
    protected String pwd;
    
    private Map<String, String> httpHeaders;
    
    protected String requestTemplate;
    
    protected List<PlaceHolderBean> placeHolders;
    
    protected IXMLProcessor resAsXMLProcessor;
    
    protected IXMLProcessor resAsStringProcessor;
    
    public SMSBaseProtocolAdatperCustProvider()
    {
        //Prepare HTTP Request Headers
        httpHeaders = new HashMap<String, String>();
        soapAction = ConfigManager.getInstance().getValue(getSOAPActionPropName());
        if (null != soapAction)
        {
            httpHeaders.put("SOAPAction", soapAction);
        }
        
        String headersConf = ConfigManager.getInstance().getValue("sms.http.gateway.http.headers");
        if (StringUtils.isNotEmpty(headersConf))
        {
            String[] headers = headersConf.split("\\|");
            for (String header : headers)
            {
                httpHeaders.put(header.split(":")[0], header.split(":")[1]);
            }
        }
        //Prepare Escape Flag
        if ("Y".equalsIgnoreCase(ConfigManager.getInstance().getValue("sms.http.gateway.xml.request.content.escaple.flag")))
        {
            contentNeedEscape = true;
        }
        
        if ("Y".equalsIgnoreCase(ConfigManager.getInstance().getValue("sms.http.gateway.xml.response.content.escaple.flag")))
        {
            resContentNeedUnescape = true;
        }
        
        //Prepare user id and password to access gateway
        userId = StringUtils.avoidNull(ConfigManager.getInstance().getValue("sms.http.gateway.user.id"));
        pwd = StringUtils.avoidNull(ConfigManager.getInstance().getValue("sms.http.gateway.user.pwd"));
        if ("Y".equalsIgnoreCase(ConfigManager.getInstance().getValue("sms.http.gateway.user.pwd.need.decrypt"))
            && StringUtils.isNotEmpty(pwd))
        {
            pwd = AESCbc128Utils.decryptPwd(userId, pwd);
        }
        
        resContentParsingWay = ConfigManager.getInstance().getValue("sms.http.gateway.xml.response.content.parsing.way");
        
        init();
    }
    
    @Override
    public Map<String, String> getRequestHeaders()
    {
        return httpHeaders;
    }
    
    @Override
    public Object preSend(Object reqMessage)
    {
        return reqMessage;
    }
    
    @Override
    public String reBuildNewUrl(String url, String interfaceName)
    {
        return url;
    }
    
    @Override
    public Object postSend(Object resMessage)
    {
        return resMessage;
    }
    
    protected void init()
    {
        readTemplate4SendMsg();
        
        readTemplate4ReceiptMsg();
    }
    
    private void readTemplate4SendMsg()
    {
        requestTemplate = ESDKIOUtils.getClasspathFileContent(getReqTemplateFileName());
    }
    
    private void readTemplate4ReceiptMsg()
    {
        try
        {
            placeHolders = (new ResponseTemplateXMLProcessor()).processClasspathXMLFile(getResTemplateFileName());
        }
        catch (ParserConfigurationException e)
        {
            LOGGER.error("", e);
        }
        catch (SAXException e)
        {
            LOGGER.error("", e);
        }
        catch (IOException e)
        {
            LOGGER.error("", e);
        }
        
        resAsXMLProcessor = new ResponseXMLProcessor(placeHolders);
        
        resAsStringProcessor = new ResponseXMLStringProcessor(placeHolders);
    }
    
    @Override
    public Object preProcessReq(Object reqMessage)
    {
        return reqMessage;
    }
    
    @Override
    public Object postBuildRes(Object resMessage, String resObjClass)
        throws ProtocolAdapterException
    {
        Object result = null;
        try
        {
            String xmlContent = (String)resMessage;
            LOGGER.debug("The response content is:" + xmlContent);
            if (resContentNeedUnescape)
            {
                xmlContent = StringUtils.unescapeXML(xmlContent);
            }
            result = parseResponse(xmlContent, resObjClass);
        }
        catch (ParserConfigurationException e)
        {
            LOGGER.error("", e);
        }
        catch (SAXException e)
        {
            LOGGER.error("Response XML content cannot be parsed", e);
            ProtocolAdapterException ex = new ProtocolAdapterException();
            throw ex;
        }
        catch (IOException e)
        {
            LOGGER.error("", e);
        }
        
        return result;
    }
    
    protected Object parseResponse(String xmlContent, String resObjClass)
        throws ParserConfigurationException, SAXException, IOException
    {
        
        List<PlaceHolderBean> placeHolders = null;
        if ("dom".equalsIgnoreCase(resContentParsingWay))
        {
            placeHolders = resAsXMLProcessor.processXML(xmlContent);
        }
        else
        {
            placeHolders = resAsStringProcessor.processXML(xmlContent);
        }
        
        if (Map.class.getName().equals(resObjClass))
        {
            Map<String, String> result = new HashMap<String, String>();
            for (PlaceHolderBean placeHolderBean : placeHolders)
            {
                result.put(placeHolderBean.getName(), placeHolderBean.getValue());
            }
            return result;
        }
        else if (List.class.getName().equals(resObjClass))
        {
            List<Map<String, String>> result = new ArrayList<Map<String, String>>();
            Map<String, String> item;
            for (PlaceHolderBean placeHolderBean : placeHolders)
            {
                item = new HashMap<String, String>();
                item.put(placeHolderBean.getName(), placeHolderBean.getValue());
                result.add(item);
            }
            return result;
        }
        else
        {
            throw new IllegalArgumentException("The response class is not supported");
        }
    }
    
    protected String fillSendMessage(String serialNo, String srcNumber, String destNumbers, int destNumbs,
        String smsContent)
    {
        String result = requestTemplate;
        result = result.replaceAll("@\\{userName\\}", userId);
        result = result.replaceAll("@\\{userpwd\\}", pwd);
        result = result.replaceAll("@\\{serialNo\\}", serialNo);
        result = result.replaceAll("@\\{sender\\}", srcNumber);
        result = result.replaceAll("@\\{reciversCount\\}", String.valueOf(destNumbs));
        result = result.replaceAll("@\\{reciverAsCVS\\}", destNumbers);
        result = result.replaceAll("@\\{SMSContent\\}", StringUtils.escapeXMLSymbols(smsContent));
        
        if (contentNeedEscape)
        {
            result = StringUtils.escapeXMLSymbols(result);
        }
        
        return result;
    }
    
    protected abstract String getSOAPActionPropName();
    
    protected abstract String getReqTemplateFileName();
    
    protected abstract String getResTemplateFileName();
}
