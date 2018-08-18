package com.ca.sport.user;

public interface SessionProvider {


    //先行提供接口
    //保存用户名到Redis中
    void setAttribuerForUsername(String name, String value);

    //取用户名从Redis中
    String getAttributeForUsername(String name);

    //验证码

    //退出登陆


}
