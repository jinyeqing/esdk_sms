package com.huawei.esdk.sms.openapi.smpp.user;

import java.util.List;

public interface ISMPPUserService
{
    User findUser(String userId);
    
    List<User> findAllUsers();
}
