package com.ca.sport.interceptor;


import com.ca.sport.user.SessionProvider;
import com.ca.sport.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截的是Controller层之前  Springmvc Handler处理器
 *
 * @author lx
 */
public class CustomInterceptor implements HandlerInterceptor {

    @Autowired
    private SessionProvider sessionProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //必须登陆
        String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(request, response));
        if (null == username) {
            //未登陆
            //重定向到登陆页面
            response.sendRedirect("http://localhost:8082/login.aspx?returnUrl=http://localhost:8081/");
            return false;
        }
        //放行 true 不放行false
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
