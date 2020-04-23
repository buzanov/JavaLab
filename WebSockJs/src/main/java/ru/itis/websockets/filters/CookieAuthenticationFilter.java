package ru.itis.websockets.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class CookieAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("USER_LOGIN")) {
                if (cookie.getValue() != null) {
                    chain.doFilter(request, response);
                } else {
                    response.getWriter().println("Authentication failed ");
                }
            }
        }
    }
}
