package ru.itis.javalab.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.javalab.config.FreemarkerConfig;
import ru.itis.javalab.model.User;
import ru.itis.javalab.service.SignUpService;

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

@WebServlet("/signUp")
public class SignUpServlet extends HttpServlet {
    private SignUpService signUpService;
    private PasswordEncoder encoder;
    private static final String template = "signUp.ftl";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        ApplicationContext springContext = (ApplicationContext) context.getAttribute("springContext");
        signUpService = springContext.getBean(SignUpService.class);
        encoder = springContext.getBean(PasswordEncoder.class);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = User.builder()
                .login(request.getParameter("username"))
                .password(encoder.encode(request.getParameter("password")))
                .email(request.getParameter("email"))
                .build();
        signUpService.signUp(user, getServletContext());
        response.sendRedirect("/signIn");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> root = new HashMap<>();
        FreemarkerConfig.preprocessConfig(response.getWriter(), root, getServletContext(), template);
    }
}
