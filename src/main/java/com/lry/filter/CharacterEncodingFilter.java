package com.lry.filter;

import java.io.IOException;

import javax.servlet.*;

public class CharacterEncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Filter初始化");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("Filter销毁");
    }
}
