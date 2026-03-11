package za.co.luban.NotificationEngine.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.luban.NotificationEngine.model.Certificates;
import za.co.luban.NotificationEngine.repository.CertificateRepository;
import za.co.luban.NotificationEngine.service.CertificateService;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Override
    public void saveCertificate(Certificates certificate) {

        certificateRepository.save(certificate);
    }
}
