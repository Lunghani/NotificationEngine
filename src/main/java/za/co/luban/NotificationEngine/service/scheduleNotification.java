package za.co.luban.NotificationEngine.service;


import org.springframework.stereotype.Service;
import za.co.luban.NotificationEngine.model.Certificates;

import java.util.Date;
import java.util.List;

@Service
public interface scheduleNotification {

   void checkCertificateExpirationDate();
}
