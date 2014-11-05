package com.huawei.esdk.sms.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 短消息数据模型
 */
public class SMSMessage implements Serializable
{
    /**
     * Serialization UID
     */
    private static final long serialVersionUID = 5560159477701711480L;
    
    /**
     * 消息的标识
     */
    private String id;
    
    /**
     * 消息类型: 0 - 要发送的短信， 1 - 从网关侧收到的短息， 2 - 从网关侧收到的状态报告
     */
    private int type;
    
    /**
     * 发送方标识，如号码
     */
    private String srcId;
    
    /**
     * 接收方方标识，如号码
     */
    private String[] destId;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 消息内容编码
     */
    private String encode;

    /**
     * 消息收到或发送的时间
     */
    private Date msgTime;
    
    /**
     * 消息收到或发送的时间以字符串形式呈现
     */
    private String msgTimeAsString;
    
    private boolean needReport;
    
    /**
     * 该消息网关报告的消息状态
     * 0为收到，其他为未收到
     */
    private String reportState;
    
    /**
     * 保留字段1
     */
    private String reserve1;
    
    /**
     * 保留字段1
     */
    private String reserve2;
    
    /**
     * 保留字段1
     */
    private String reserve3;
    
    /**
     * 发送失败次数
     */
    private int sendFailedCount;
    
    /**
     * 消息发发送时间
     */
    private Date lastSendTime;
    
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getSrcId()
    {
        return srcId;
    }

    public void setSrcId(String srcId)
    {
        this.srcId = srcId;
    }

    public String getDestIdAsString()
    {
        if (destId == null)
        {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (String str : destId)
        {

            sb.append(',').append(str);
        }
        if (sb.length() > 0)
        {
            return sb.toString().substring(1);
        }
        else
        {
            return sb.toString();
        }
    }
    
    public String[] getDestId()
    {
        return destId;
    }

    public void setDestId(String[] destId)
    {
        this.destId = destId;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getEncode()
    {
        return encode;
    }

    public void setEncode(String encode)
    {
        this.encode = encode;
    }

    public Date getMsgTime()
    {
        return msgTime;
    }

    public void setMsgTime(Date msgTime)
    {
        this.msgTime = msgTime;
    }

    public String getMsgTimeAsString()
    {
        return msgTimeAsString;
    }

    public void setMsgTimeAsString(String msgTimeAsString)
    {
        this.msgTimeAsString = msgTimeAsString;
    }
    
    public boolean isNeedReport()
    {
        return needReport;
    }

    public void setNeedReport(boolean needReport)
    {
        this.needReport = needReport;
    }

    public String getReportState()
    {
        return reportState;
    }

    public void setReportState(String reportState)
    {
        this.reportState = reportState;
    }

    public String getReserve1()
    {
        return reserve1;
    }

    public void setReserve1(String reserve1)
    {
        this.reserve1 = reserve1;
    }
    
    public String getReserve2()
    {
        return reserve2;
    }

    public void setReserve2(String reserve2)
    {
        this.reserve2 = reserve2;
    }

    public String getReserve3()
    {
        return reserve3;
    }

    public void setReserve3(String reserve3)
    {
        this.reserve3 = reserve3;
    }
    
    public int getSendFailedCount()
    {
        return sendFailedCount;
    }

    public void setSendFailedCount(int sendFailedCount)
    {
        this.sendFailedCount = sendFailedCount;
    }

    public Date getLastSendTime()
    {
        return lastSendTime;
    }

    public void setLastSendTime(Date lastSendTime)
    {
        this.lastSendTime = lastSendTime;
    }

    @Override
    public String toString()
    {
        return "SMSMessage [type=" + type + ", srcId=" + srcId + ", destId="
                + getDestIdAsString() + ", msgTime=" + msgTime
                + ", msgTimeAsString=" + msgTimeAsString + "]";
    }
    
    public String getSMS4Logging()
    {
        StringBuilder sb = new StringBuilder("id=").append(id);
        sb.append(", From=").append(srcId);
        sb.append(", To=").append(getDestIdAsString());
        sb.append(", ReceivedTime=").append(msgTime);
        if (null != content)
        {
            sb.append(", MessageLength=").append(content.length());
        }
        
        return sb.toString();
    }
}
