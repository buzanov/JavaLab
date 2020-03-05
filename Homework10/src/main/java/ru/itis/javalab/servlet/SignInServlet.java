package ru.itis.javalab.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.javalab.config.FreemarkerConfig;
import ru.itis.javalab.model.User;
import ru.itis.javalab.service.SignInService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/signIn")
public class SignInServlet extends HttpServlet {
    private static final String template = "signIn.ftl";
    private SignInService signInService;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        ApplicationContext springContext = (ApplicationContext) context.getAttribute("springContext");
        signInService = springContext.getBean(SignInService.class);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = User.builder()
                .email(request.getParameter("email"))
                .password(request.getParameter("password"))
                .build();
        if (signInService.signIn(user)) {
            request.getSession(true).setAttribute("current_user_email", user.getEmail());
            response.sendRedirect("/profile");
        } else {
            response.sendRedirect("/signIn");
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> root = new HashMap<>();
        FreemarkerConfig.preprocessConfig(response.getWriter(), root, getServletContext(), template);
    }
}
