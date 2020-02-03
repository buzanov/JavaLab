package ru.javalab.homework3;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class MyFilter implements Filter {
    private File file;
    private PrintWriter pw;

    public void init(FilterConfig filterConfig) throws ServletException {
        file = new File("stats.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            pw = new PrintWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        String ipAddress = servletRequest.getRemoteAddr();
        String method = ((HttpServletRequest) servletRequest).getMethod();
        pw.write("time: " + new Date().toString() + "\n");
        pw.write("method: " + method + "\n");
        pw.write("Address: " + httpReq.getRequestURL() + "\n");
        pw.write("IP address: " + ipAddress + "\n");
        pw.write("-------------------------------------------------------------------" + "\n");
        pw.flush();
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {

    }
}
