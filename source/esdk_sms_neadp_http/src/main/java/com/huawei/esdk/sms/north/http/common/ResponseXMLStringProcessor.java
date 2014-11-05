package com.huawei.esdk.sms.north.http.common;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.huawei.esdk.sms.north.http.bean.PlaceHolderBean;

public class ResponseXMLStringProcessor extends AbstractXMLProcessor
{
    private List<PlaceHolderBean> placeHolders;
    
    public ResponseXMLStringProcessor(List<PlaceHolderBean> placeHolders)
    {
        this.placeHolders = placeHolders;
    }
    
    protected List<PlaceHolderBean> parseXML(String xmlAsString)
    {
        List<PlaceHolderBean> result = new ArrayList<PlaceHolderBean>();
        
        int startIndex, endIndex, index;
        PlaceHolderBean item;
        String tagName, endTagName;
        for (PlaceHolderBean placeHolder : placeHolders)
        {
            index = 0;
            tagName = "<" + placeHolder.getTagName() + ">";
            endTagName = "</" + placeHolder.getTagName() + ">";
            do
            {
                //TODO
                startIndex = xmlAsString.indexOf(tagName, index);
                if (-1 == startIndex)
                {
                    startIndex = getTagValueStartIndex(placeHolder.getTagName(), xmlAsString, index);
//                    if (-1 != startIndex)
//                    {
//                        
//                    }
                }
                endIndex = xmlAsString.indexOf(endTagName, index + tagName.length());
                if (endIndex > startIndex)
                {
                    item = new PlaceHolderBean();
                    item.setName(placeHolder.getName());
                    item.setTagName(placeHolder.getTagName());
                    item.setValue(xmlAsString.substring(xmlAsString.indexOf(">", startIndex) + 1, endIndex));
                    result.add(item);
                    
                    index = endIndex;
                }
            }
            while(startIndex >-1);
        }
        
        return result;
    }
    
    private int getTagValueStartIndex(String tagName, String xmlAsString, int index)
    {
        return xmlAsString.indexOf("<" + tagName, index);
    }
    
    @Override
    protected PlaceHolderBean processElement(Element element)
    {
        return null;
    }
}
