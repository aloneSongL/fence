package com.bs.fence.filter;

import com.bs.fence.common.BaseContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.DigestUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sjx
 * @Description 检查用户是否已经完成登录
 */
//@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取本次请求的URI
        String requestURI = request.getRequestURI();

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/page/register",
                "/css/**",
                "/js/**",
                "/user/**",
                "/favicon.ico"
        };

        //判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //如果不需要处理，则直接放行
        if(check){
            filterChain.doFilter(request,response);
            return;
        }

//        判断登录状态，如果已登录，则直接放行
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies){
                if(cookie.getName().equals("isLogin")){
                    String value = cookie.getValue();
                    if(value.equals(DigestUtils.md5DigestAsHex("yes".getBytes()))){
                        filterChain.doFilter(request,response);
                        return;
                    }
                }
            }
        }

        //如果登录成功，则放行
        Object userId = request.getSession().getAttribute("userId");
        if(userId != null){
            filterChain.doFilter(request, response);
            return;
        }

        //如果未登录则返回登录界面
        request.getSession().setAttribute("message", "请先登录才能访问此页面");
        String newURI = "/page/register";
        response.sendRedirect(newURI);
    }

    /**
    @author sjx
    @Description 路径匹配，检查本次请求是否需要放行
    @since 2022-07-15 21-25
    */
    public boolean check(String[] urls,String requestURI){
        for(String url : urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}