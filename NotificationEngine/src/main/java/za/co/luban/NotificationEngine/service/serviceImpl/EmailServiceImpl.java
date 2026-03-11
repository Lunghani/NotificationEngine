package za.co.luban.NotificationEngine.service.serviceImpl;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import za.co.luban.NotificationEngine.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender = new JavaMailSenderImpl();

    @Override
    public void sendNotification(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("do-not-reply@ndhuvi.co.za");

        mailSender.send(message);
    }
}
