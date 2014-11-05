package com.huawei.esdk.sms.north.http.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.huawei.esdk.platform.common.utils.ESDKIOUtils;
import com.huawei.esdk.platform.common.utils.help.DocumentBuilderFactories;
import com.huawei.esdk.sms.north.http.bean.PlaceHolderBean;

public abstract class AbstractXMLProcessor implements IXMLProcessor
{
    private static Logger LOGGER = Logger.getLogger(AbstractXMLProcessor.class);
    
    @Override
    public List<PlaceHolderBean> processClasspathXMLFile(String fileName)
        throws ParserConfigurationException, SAXException, IOException
    {
        String xmlContent = ESDKIOUtils.getClasspathFileContent(fileName);
        return parseXML(xmlContent);
    }
    
    @Override
    public List<PlaceHolderBean> processXML(String xmlContent)
        throws ParserConfigurationException, SAXException, IOException
    {
        return parseXML(xmlContent);
    }
    
    protected List<PlaceHolderBean> parseXML(String xmlAsString)
        throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactories.newSecurityInstance();

        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(xmlAsString.getBytes("utf-8"))));
        doc.getDocumentElement().normalize();
        Element rootElement = doc.getDocumentElement();
        List<PlaceHolderBean> result = new ArrayList<PlaceHolderBean>();
        return parseNode(rootElement, result);
    }
    
    protected List<PlaceHolderBean> parseNode(Node nNode, List<PlaceHolderBean> placerHolders)
    {
        StringBuilder sb = new StringBuilder();
        if (LOGGER.isDebugEnabled())
        {
            sb.append("Current Node :").append(nNode.getNodeName());
            sb.append("|Node Type:").append(nNode.getNodeType());
            sb.append("|Node Value:").append(nNode.getNodeValue());
            sb.append("|Text Value:" + nNode.getTextContent());
            LOGGER.debug(sb.toString());
        }
        
        if (nNode.getNodeType() == Node.ELEMENT_NODE)
        {
            Element eElement = (Element)nNode;
            if (hasSubElement(nNode))
            {
                NodeList nList = nNode.getChildNodes();
                Node nodeItem;
                for (int temp = 0; temp < nList.getLength(); temp++)
                {
                    nodeItem = nList.item(temp);
                    parseNode(nodeItem, placerHolders);
                }
            }
            else
            {
                if (LOGGER.isDebugEnabled())
                {
                    sb.delete(0, sb.length());
                    sb.append("Tag Name:").append(eElement.getTagName());
                    sb.append("|Node Name:").append(eElement.getNodeName());
                    sb.append("|Node Value:").append(eElement.getNodeValue());
                    sb.append("|Text Content:").append(eElement.getTextContent());
                    LOGGER.debug(sb.toString());
                }
                //It's the element which hasn't child element and should be processed
                PlaceHolderBean placeHolder = processElement(eElement);
                if (null != placeHolder)
                {
                    placerHolders.add(placeHolder);
                }
            }
        }
        
        return placerHolders;
    }
    
    private boolean hasSubElement(Node node)
    {
        if (null == node || Node.ELEMENT_NODE != node.getNodeType())
        {
            return false;
        }
        
        NodeList nList = node.getChildNodes();
        Node nodeItem;
        for (int temp = 0; temp < nList.getLength(); temp++)
        {
            nodeItem = nList.item(temp);
            if (Node.ELEMENT_NODE == nodeItem.getNodeType())
            {
                return true;
            }
        }
        
        return false;
    }    
    
    protected abstract PlaceHolderBean processElement(Element element);
}
