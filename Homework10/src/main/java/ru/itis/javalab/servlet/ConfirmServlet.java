package ru.itis.javalab.servlet;

import org.springframework.context.ApplicationContext;
import ru.itis.javalab.repository.UserRepository;
import ru.itis.javalab.service.EmailConfirmationService;
import ru.itis.javalab.service.EmailConfirmationServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/confirm")
public class ConfirmServlet extends HttpServlet {
    private EmailConfirmationService emailConfirmationService;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        ApplicationContext springContext = (ApplicationContext) context.getAttribute("springContext");
        emailConfirmationService = springContext.getBean(EmailConfirmationService.class);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        emailConfirmationService.confirm(request.getParameter("token"));
        response.sendRedirect("/profile");
    }
}
