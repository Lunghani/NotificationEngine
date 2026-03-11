package za.co.luban.NotificationEngine.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {

   void sendNotification(String to, String subject, String body);
}
