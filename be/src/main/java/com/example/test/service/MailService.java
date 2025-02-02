package com.example.test.service;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.test.repository.JobRepository;

import jakarta.mail.MessagingException;

import jakarta.mail.internet.MimeMessage;

import com.example.test.domain.Job;
import com.example.test.domain.response.ResponseEmailJob;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MailService {

    @Value("${frontend.url}")
    private String frontendUrl;

    private final MailSender mailSender;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final JobRepository jobRepository;

    public MailService(MailSender mailSender, JavaMailSender javaMailSender, SpringTemplateEngine templateEngine,
            JobRepository jobRepository) {
        this.mailSender = mailSender;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.jobRepository = jobRepository;
    }

    public void sendMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("test@gmail.com");
        message.setSubject("Test mail");
        message.setText("Test mail content");
        this.mailSender.send(message);
    }

    public void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendEmailFromTemplateSync(String to, String subject, String templateName, String name, Object object) {
        Context context = new Context();

        context.setVariable("jobs", object);
        context.setVariable("name", name);
        context.setVariable("frontendUrl", frontendUrl);

        String content = templateEngine.process(templateName, context);
        this.sendEmailSync(to, subject, content, false, true);
    }

    @Async
    public void sendOtpEmail(String email, String otp) {
        Context context = new Context();

        context.setVariable("otp", otp);

        String content = templateEngine.process("otp.template", context);

        this.sendEmailSync(email, "Lấy lại mật khẩu", content, false, true);

    }

    @Async
    public void sendNewPassword(String password, String email) {
        Context context = new Context();
        context.setVariable("password", password);

        String content = templateEngine.process("new-password.template", context);

        this.sendEmailSync(email, "Mật khẩu mới", content, false, true);
    }

}
