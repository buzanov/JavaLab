package ru.itis.javalab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Component
public class MessageSenderImpl implements MessageSender {
    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public void sendMessage(String target, String subject, String text) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setTo(target);
            helper.setText(text, true);
            helper.setSubject(subject);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
