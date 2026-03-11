package za.co.luban.NotificationEngine.service;

import org.springframework.stereotype.Service;
import za.co.luban.NotificationEngine.model.Certificates;

@Service
public interface CertificateService {

    void saveCertificate(Certificates certificate);
}
