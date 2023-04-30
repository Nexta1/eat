package com.zhx.eat.filter;

import com.alibaba.fastjson.JSON;
import com.zhx.eat.common.BaseContext;
import com.zhx.eat.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "login", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("拦截:{}", request.getRequestURI());
        // 设置不要拦截的请求
        String[] urls = new String[]{
                "/employee/login",
                "/employee/layout",
                "/backend/**",
                "/front/**",
                "/swagger-ui.html",
                "/user/code",
                "/user/login"
        };

        if (match(request.getRequestURI(), urls)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (request.getSession().getAttribute("employee") != null) {

            Long empId = (Long) request.getSession().getAttribute("employee");
            log.info("{}", empId);
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
     
    }

    // 匹配url
    public boolean match(String url, String[] urls) {
        for (String item : urls) {
            if (PATH_MATCHER.match(item, url)) {
                return true;
            }
        }
        return false;
    }

}
