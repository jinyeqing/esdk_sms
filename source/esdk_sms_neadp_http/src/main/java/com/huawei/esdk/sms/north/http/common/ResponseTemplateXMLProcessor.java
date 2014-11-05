package com.huawei.esdk.sms.north.http.common;

import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Element;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.sms.north.http.bean.PlaceHolderBean;

public class ResponseTemplateXMLProcessor extends AbstractXMLProcessor
{
    private List<String> placeHolders;
    
    public ResponseTemplateXMLProcessor()
    {
        String confPlaceHolders = ConfigManager.getInstance().getValue("sms.http.response.template.placeholders");
        if (null == confPlaceHolders)
        {
            confPlaceHolders = "";
        }
        placeHolders = Arrays.asList(confPlaceHolders.split(","));
    }
    
    @Override
    public PlaceHolderBean processElement(Element element)
    {        
        String textContent = element.getTextContent();
        
        if (null != textContent)
        {
            //Logic Here please
            String placeHolder = textContent.trim();
            if (placeHolders.contains(placeHolder))
            {
                PlaceHolderBean placeHolderBean = new PlaceHolderBean();
                placeHolderBean.setName(placeHolder);
                placeHolderBean.setTagName(element.getTagName());
                return placeHolderBean;
            }
        }
        
        return null;
    }
}
