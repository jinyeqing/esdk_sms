package com.huawei.esdk.sms.north.http.common;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.huawei.esdk.sms.north.http.bean.PlaceHolderBean;

public interface IXMLProcessor
{
    public List<PlaceHolderBean> processClasspathXMLFile(String fileName)
        throws ParserConfigurationException, SAXException, IOException;
    
    public List<PlaceHolderBean> processXML(String xmlContent)
        throws ParserConfigurationException, SAXException, IOException;
}
