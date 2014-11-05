package com.huawei.esdk.sms.core.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.DateUtils;
import com.huawei.esdk.platform.common.utils.PatternUtils;
import com.huawei.esdk.platform.common.utils.PlatformSMSLogUtils;
import com.huawei.esdk.platform.common.utils.StringUtils;
import com.huawei.esdk.sms.constants.SMSConstants;
import com.huawei.esdk.sms.core.IDispatcher;
import com.huawei.esdk.sms.core.IMsgCallback;
import com.huawei.esdk.sms.core.IRecvSMSCallback;
import com.huawei.esdk.sms.core.ISMSAdapter;
import com.huawei.esdk.sms.core.impl.helper.RoutingResult;
import com.huawei.esdk.sms.exception.SDKSMSException;
import com.huawei.esdk.sms.model.SMSDeliveryStatus;
import com.huawei.esdk.sms.model.SMSMessage;
import com.huawei.esdk.sms.resend.FailedSMSInfo;
import com.huawei.esdk.sms.resend.ReSendEngine;

public class Dispatcher implements IDispatcher, IRecvSMSCallback
{
    private static final Logger LOGGER = Logger.getLogger(Dispatcher.class);
    
    private static final Map<String, Pattern> PATTERNS = new HashMap<String, Pattern>();
    
    private ReSendEngine reSendEngine;
    
    /**
     * It's injected through Spring configuration
     */
    private List<ISMSAdapter> smsAdapters = new ArrayList<ISMSAdapter>(4);
    
    /**
     * It's injected through Spring configuration
     */
    private List<IMsgCallback> msgCallbacks = new ArrayList<IMsgCallback>(4);
    
    @Override
    public String sendSMSMessage(SMSMessage smsMessage)
        throws SDKSMSException
    {
        String errorMsg;
        if (null == smsMessage)
        {
            throw new IllegalArgumentException("The incoming SMS Message is null");
        }
        
        
        if (1 == smsAdapters.size())
        {
            return doSendSMS(smsAdapters.get(0), smsMessage);
        }
        else if (smsAdapters.size() > 1)
        {
            for (int i = 0; i < smsAdapters.size(); i++)
            {
                // 配置为二次开发方式时，其它发送方式无效
                if ("com.huawei.esdk.sms.device.plugin.SMSAdapterDevicePlugin".equals(smsAdapters.get(i)
                    .getClass()
                    .getName()))
                {
                    return doSendSMS(smsAdapters.get(i), smsMessage);
                }
            }
            return sendSMSWithRoutingLogic(smsMessage);
        }
        else
        {
            errorMsg = "There is not any SMS adapters for this SMS message " + smsMessage.getSMS4Logging();
            LOGGER.warn(errorMsg);
            throw new SDKSMSException(errorMsg);
        }
    }
    
    protected String sendSMSWithRoutingLogic(SMSMessage smsMessage) throws SDKSMSException
    {
        RoutingResult routingResult = getsmsAdapter(smsMessage);
        String result = null;
        String tempResult = null;
        String errorMsg = null;
        if (routingResult.getAdapters().size() > 0)
        {
            int index = 0;
            String[] tempArry;
            List<String> tempList;
            for (ISMSAdapter adapter : routingResult.getAdapters())
            {
                tempList = routingResult.getAdaptersMappingNumbers().get(index++);
                tempArry = new String[tempList.size()];
                tempList.toArray(tempArry);
                smsMessage.setDestId(tempArry);
                tempResult = doSendSMS(adapter, smsMessage);
                if (null == result)
                {
                    result = tempResult;
                }
            }
            
            for (String unmatchedNumber : routingResult.getUnMatchedMobileNumbers())
            {
                PlatformSMSLogUtils.writeSendErrorLog("Mobile number " + unmatchedNumber
                    + " cannot be matched to a proper gateway to send out");
            }
            
            return result;
        }
        else
        {
            errorMsg = "No adapter can be selected, please check your configuration.";
            LOGGER.warn(errorMsg);
            throw new SDKSMSException(errorMsg);
        }
    }
    
    protected String doSendSMS(ISMSAdapter smsAdapter, SMSMessage smsMessage)
        throws SDKSMSException
    {
        try
        {
            String smsIdentifier = smsAdapter.sendSMSMessage(smsMessage).getValue();
            
            return smsIdentifier;
        }
        catch (SDKSMSException e)
        {
            if (SMSConstants.SMS_SEND_ERROR == e.getSdkErrCode())
            {
                //发送失败，进入失败重发队列
                FailedSMSInfo failedSMS = new FailedSMSInfo();
                failedSMS.setSmsAdapter(smsAdapter);
                failedSMS.setSmsMessage(smsMessage);
                smsMessage.setLastSendTime(DateUtils.getCurrentDate());
                reSendEngine.intoQueue(failedSMS);
            }
            throw e;
        }
    }
    
    /**
     * 发送消息回执
     * 由eSDK实现
     * @param smsDeliveryStatus
     * @throws SDKSMSException
     */
    @Override
    public void onRecvReceipt(SMSDeliveryStatus smsDeliveryStatus)
        throws SDKSMSException
    {
        if (1 == msgCallbacks.size())
        {
            //如果同时接多个网关，暂不支持状态报告
            if (smsAdapters.size() <= 1)
            {
                msgCallbacks.get(0).onSmsDeliveryStatus(smsDeliveryStatus);
            }
        }
        else if (msgCallbacks.size() > 1)
        {
            //Do the routing - 多应用这里可能会有逻辑
        }
        else
        {
            String errorMsg = "There is no message callbacks to delevery receving SMS";
            LOGGER.warn(errorMsg);
            throw new SDKSMSException(errorMsg);
        }
    }
    
    @Override
    public void onRecvSendResp()
    {
    }
    
    /**
     * eSDK收到ISV（短信网关）发来的短信
     * @param smsMessage
     * @throws SDKSMSException
     */
    @Override
    public void onRecvIncomeSMS(SMSMessage smsMessage)
        throws SDKSMSException
    {
        if (1 == msgCallbacks.size())
        {
            msgCallbacks.get(0).onSmsReception(smsMessage);
        }
        else if (msgCallbacks.size() > 1)
        {
            //Do the routing - 多应用这里可能会有逻辑
        }
        else
        {
            String errorMsg = "There is no message callbacks to delevery receving SMS";
            LOGGER.warn(errorMsg);
            throw new SDKSMSException(errorMsg);
        }
    }
    
    private RoutingResult getsmsAdapter(SMSMessage sms)
    {
        RoutingResult result = new RoutingResult();
        
        String propKey;
        String propValue;
        Pattern pattern;
        List<String> numbers;
        List<String> origNumbers = new ArrayList<String>(Arrays.asList(sms.getDestId()));
        List<String> leftNumbers;
        for (ISMSAdapter smsAdapter : smsAdapters)
        {
            if (origNumbers.size() <= 0)
            {
                break;
            }
            numbers = new ArrayList<String>();
            propKey = "sms.gateway." + smsAdapter.getSMSAdapterId() + ".mobile.number.regex";
            pattern = PATTERNS.get(propKey);
            if (null == pattern)
            {
                propValue = ConfigManager.getInstance().getValue(propKey);
                if (StringUtils.isNotEmpty(propValue))
                {
                    pattern = Pattern.compile(propValue);
                    PATTERNS.put(propKey, pattern);
                }
                else
                {
                    continue;
                }
            }
            
            leftNumbers = new ArrayList<String>();
            leftNumbers.addAll(origNumbers);
            //Loop numbers
            for (String mobileNumber : leftNumbers)
            {
                if (PatternUtils.isMatch(mobileNumber, pattern))
                {
                    numbers.add(mobileNumber);
                    origNumbers.remove(mobileNumber);
                }                
            }
            if (numbers.size() > 0)
            {
                result.getAdapters().add(smsAdapter);
                result.getAdaptersMappingNumbers().add(numbers);
            }
        }
        
        result.getUnMatchedMobileNumbers().addAll(origNumbers);
        
        return result;
    }
    
    public List<IMsgCallback> getMsgCallbacks()
    {
        return msgCallbacks;
    }
    
    public void setMsgCallbacks(List<IMsgCallback> msgCallbacks)
    {
        this.msgCallbacks = msgCallbacks;
    }
    
    public List<ISMSAdapter> getSmsAdapters()
    {
        return smsAdapters;
    }
    
    public void setSmsAdapters(List<ISMSAdapter> smsAdapters)
    {
        this.smsAdapters = smsAdapters;
    }
    
    public ReSendEngine getReSendEngine()
    {
        return reSendEngine;
    }
    
    public void setReSendEngine(ReSendEngine reSendEngine)
    {
        this.reSendEngine = reSendEngine;
    }
}
