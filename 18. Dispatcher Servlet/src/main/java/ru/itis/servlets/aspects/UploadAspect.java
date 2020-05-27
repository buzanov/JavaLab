package ru.itis.servlets.aspects;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.servlets.models.FileInfo;
import ru.itis.servlets.services.MessageSender;


@Aspect
@Component
public class UploadAspect {
    @Autowired
    MessageSender messageSender;

    @After(value = "execution(* ru.itis.servlets.services.FileSaverImpl.save(..))")
    public void after(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String email = (String) args[2];
        System.out.println("ENTERED");
        FileInfo fileInfo = (FileInfo) args[1];
        messageSender.sendMessage(email, "File successfully uploaded", "Link for uploaded file: " + fileInfo.getUrl());
    }
}
