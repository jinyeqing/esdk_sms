package com.huawei.esdk.sms.openapi.smpp.user;

import java.util.ArrayList;
import java.util.List;

import com.huawei.esdk.platform.common.config.ConfigManager;
import com.huawei.esdk.platform.common.utils.AESCbc128Utils;
import com.huawei.esdk.platform.common.utils.StringUtils;

public class SMPPUserService implements ISMPPUserService
{
    private static SMPPUserService instance = new SMPPUserService();
    
    private List<User> users;
    
    private SMPPUserService()
    {
        users = new ArrayList<User>();
        String userConf = ConfigManager.getInstance().getValue("esdk.smpp.server.login.name");
        String pwdConf = StringUtils.avoidNull(ConfigManager.getInstance().getValue("esdk.smpp.server.password"));
        
        if (null != userConf)
        {
            String[] usersArray = userConf.split(",");
            String[] pwdArray = pwdConf.split(",");
            User user;
            for (int i = 0; i < usersArray.length; i++)
            {
                user = new User();
                user.setUserId(usersArray[i]);
                if (i < pwdArray.length)
                {
                    user.setPwd(AESCbc128Utils.decryptPwd(usersArray[i], pwdArray[i]));
                }
                
                users.add(user);
            }
        }
    }
    
    public static SMPPUserService getInstance()
    {
        return instance;
    }
    
    @Override
    public User findUser(String userId)
    {
        if (null != users)
        {
            for (User item : users)
            {
                if (item.getUserId().equals(userId))
                {
                    return item;
                }
            }
        }
        return null;
    }
    
    @Override
    public List<User> findAllUsers()
    {
        return users;
    }
}
