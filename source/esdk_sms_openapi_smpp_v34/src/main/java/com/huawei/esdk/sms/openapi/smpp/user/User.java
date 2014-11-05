package com.huawei.esdk.sms.openapi.smpp.user;

public class User
{
    private String userId;
    
    private String pwd;
    
    private String loginFlag;

    private String status;
    
    private String remarks;
    
    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getPwd()
    {
        return pwd;
    }

    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }

    public String getLoginFlag()
    {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag)
    {
        this.loginFlag = loginFlag;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }
}
