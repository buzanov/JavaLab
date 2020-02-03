package ru.javalab.homework3;

import main.java.ru.itis.urlDownloader.Downloader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.write("<html>" +
                "<body>" +
                "<form method= \"post\" action=\"download\">" +
                "<input name=\"url\" type=\"text\">" +
                "<input type=\"submit\">" +
                "</form>" +
                "</body>" +
                "</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getParameter("url");
        Downloader downloader = new Downloader(url, "");
        downloader.download();
        resp.sendRedirect("download");
    }
}
