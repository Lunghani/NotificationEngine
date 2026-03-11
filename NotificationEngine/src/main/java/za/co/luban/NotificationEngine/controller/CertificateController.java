package za.co.luban.NotificationEngine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.luban.NotificationEngine.model.Certificates;
import za.co.luban.NotificationEngine.service.CertificateService;

@RestController
@RequestMapping("/certificate/api/v1")
@CrossOrigin("*")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping("/save-certificate")
    public ResponseEntity<?> saveCertificate(@RequestBody Certificates certificate) {
        certificateService.saveCertificate(certificate);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Certificate has been saved successfully");
    }
}

