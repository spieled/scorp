package cn.studease.scorp.servlet;

import java.io.IOException;
import javax.servlet.*;

/**
 * Author: liushaoping
 * Date: 2015/5/12.
 */
public class HelloServlet implements Servlet {
    public static void main(String[] args) {
        System.out.println("hello world");
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

        System.out.println("HelloServlet is in processing ....");
        servletResponse.getWriter().append("HelloServlet is in processing ....").flush();

    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
