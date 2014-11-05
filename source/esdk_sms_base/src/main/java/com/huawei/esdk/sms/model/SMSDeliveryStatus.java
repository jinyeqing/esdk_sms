package com.huawei.esdk.sms.model;

import java.util.List;

public class SMSDeliveryStatus
{
    private String id;
    
    private List<SMSStatus> statusList;
    
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public List<SMSStatus> getStatusList()
    {
        return statusList;
    }

    public void setStatusList(List<SMSStatus> statusList)
    {
        this.statusList = statusList;
    }
}
