package com.lry.filter;

import java.io.IOException;

import javax.servlet.*;

/**
 * @author 刘汝杨
 */
public class CssEncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setContentType("text/css;charset=utf-8");
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
