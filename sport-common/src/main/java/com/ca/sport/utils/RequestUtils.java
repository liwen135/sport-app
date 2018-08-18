package com.ca.sport.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 获取CSESSIONID
 *
 * @author lx
 */
public class RequestUtils {

    //获取
    public static String getCSESSIONID(HttpServletRequest request, HttpServletResponse response) {
        //1：取出Cookie
        Cookie[] cookies = request.getCookies();
        if (null != cookies && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                //2: 判断COokie中是否有CSESSIONID
                if ("CSESSIONID".equals(cookie.getName())) {
                    //3:有  直接使用
                    return cookie.getValue();
                }
            }
        }
        //4:没有  创建一个CSESSIONID   并保存到COOKIE中  同时 把此COOKIe写回浏览器  使用此生成的CSESSIONID
        String csessionid = UUID.randomUUID().toString().replaceAll("-", "");
        Cookie cookie = new Cookie("CSESSIONID", csessionid);
        //设置 存活时间      -1  0   >0
        cookie.setMaxAge(-1);
        //设置路径
        cookie.setPath("/");
        //设置跨域  localhost == www.babasport.com   www.jd.com search.jd.com  item.jd.com
        //cookie.setDomain(".jd.com");
        response.addCookie(cookie);
        return csessionid;

    }
}
