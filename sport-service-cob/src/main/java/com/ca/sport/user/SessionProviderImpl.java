package com.ca.sport.user;

import com.ca.sport.web.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

/**
 * 保存用户名或验证码到Redis中
 * Session共享
 *
 * @author lx
 */
public class SessionProviderImpl implements SessionProvider {

    @Autowired
    private Jedis jedis;
    private Integer exp = 30;

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    @Override
    public void setAttribuerForUsername(String name, String value) {
        //保存用户名到Redis中
        //　K　：　CSESSIONID:Constants.USER_NAME   == name
        jedis.set(name + ":" + Constants.USER_NAME, value);
        //时间
        jedis.expire(name + ":" + Constants.USER_NAME, 60 * exp);
    }

    @Override
    public String getAttributeForUsername(String name) {
        String value = jedis.get(name + ":" + Constants.USER_NAME);
        if (null != value) {
            //时间
            jedis.expire(name + ":" + Constants.USER_NAME, 60 * exp);
        }
        return value;
    }

}
