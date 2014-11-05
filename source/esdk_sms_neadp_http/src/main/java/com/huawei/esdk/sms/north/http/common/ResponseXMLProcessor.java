package com.huawei.esdk.sms.north.http.common;

import java.util.List;

import org.w3c.dom.Element;

import com.huawei.esdk.sms.north.http.bean.PlaceHolderBean;

public class ResponseXMLProcessor extends AbstractXMLProcessor
{
    private List<PlaceHolderBean> placeHolders;
    
    public ResponseXMLProcessor(List<PlaceHolderBean> placeHolders)
    {
        this.placeHolders = placeHolders;
    }
    
    @Override
    protected PlaceHolderBean processElement(Element element)
    {
        String tagName = element.getTagName();
        PlaceHolderBean placeHolder = getPlaceHolderBeanByTag(tagName);
        if (null != placeHolder)
        {
            String textContent = element.getTextContent();
            if (null != textContent)
            {
                textContent = textContent.trim();
            }
            placeHolder.setValue(textContent);
        }
        
        return placeHolder;
    }
    
    private PlaceHolderBean getPlaceHolderBeanByTag(String tagName)
    {
        if (null == tagName)
        {
            return null;
        }
        
        for (PlaceHolderBean placeHolder : placeHolders)
        {
            if (tagName.equals(placeHolder.getTagName()))
            {
                PlaceHolderBean result = new PlaceHolderBean();
                result.setName(placeHolder.getName());
                result.setTagName(tagName);
                return result;
            }
        }
        
        return null;
    }
}
