package za.co.luban.NotificationEngine.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import za.co.luban.NotificationEngine.model.Certificates;
import za.co.luban.NotificationEngine.model.User;
import za.co.luban.NotificationEngine.repository.CertificateRepository;
import za.co.luban.NotificationEngine.repository.UserRepository;
import za.co.luban.NotificationEngine.service.SendGridEmailService;
import za.co.luban.NotificationEngine.service.scheduleNotification;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleNotificationImpl implements scheduleNotification {

    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private SendGridEmailService sendGridEmailService;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Scheduled(cron = "${CRON_JOB}")
//     "0 0 8,12 * * *"
    public void checkCertificateExpirationDate() {

        Date currentDate = new Date();

        // Normalize time to midnight
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date normalizedCurrentDate = calendar.getTime();

        // Calculate date three months later
        calendar.add(Calendar.MONTH, 3);
        Date threeMonthsLater = calendar.getTime();

        // Log the dates for verification
        System.out.println("Normalized Current Date: " + normalizedCurrentDate);
        System.out.println("Three Months Later: " + threeMonthsLater);

        List<Certificates> certificates =
                certificateRepository.findCertificatesByExpirationDateBetween(currentDate, threeMonthsLater);

        List<User> notifyList = userRepository.findAll()
                .stream()
                .filter(User::isNotify).toList();


        StringBuilder tableRows = new StringBuilder();

        for (Certificates cert : certificates) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // adjust if your date format is different

            LocalDate expirationDate = LocalDate.parse(cert.getExpirationDate());
            long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), expirationDate);
            String bgColor;
            String status;


            if (daysLeft <= 15) {
                bgColor = "#f8d7da";
                status = "Critical";
            } else if (daysLeft <= 30) {
                bgColor = "#fff3cd";
                status = "Warning";
            } else {
                bgColor = "#ffffff";
                status = "Notice";
            }

            tableRows.append(String.format(
                    "<tr style=\"background-color: %s;\">" +
                            "<td>%s</td>" +
                            "<td>%s</td>" +
                            "<td>%d</td>" +
                            "<td class=\"alert\">%s</td>" +
                            "</tr>",
                    bgColor,
                    cert.getCertificateId(),
                    cert.getExpirationDate(),
                    daysLeft,
                    status
            ));
        }
        for (User user : notifyList) {
            String message = String.format("""
                    <!DOCTYPE html>
                    <html>
                    <head>
                      <meta charset="UTF-8">
                      <title>Certificate Expiry Alert</title>
                      <meta name="viewport" content="width=device-width, initial-scale=1.0">
                      <style>
                        body { font-family: Arial, sans-serif; background-color: #f7f7f7; }
                        .container { max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; }
                        h1 { color: #333333; }
                        p { line-height: 1.6; color: #555555; }
                        table { width: 100%%; border-collapse: collapse; margin: 20px 0; }
                        th, td { border: 1px solid #dddddd; padding: 8px; text-align: left; }
                        th { background-color: #f2f2f2; }
                        .alert { color: #c00; font-weight: bold; }
                        .footer { font-size: 12px; color: #777777; text-align: center; margin-top: 20px; }
                      </style>
                    </head>
                    <body>
                      <div class="container">
                        <h1>🚨 Certificate Expiry Warning</h1>
                        <p>Hello %s,</p>
                        <p>You have the following certificate(s) expiring within the next %d months:</p>
                    
                        <table>
                          <thead>
                            <tr>
                              <th>Certificate</th>
                              <th>Expiry Date</th>
                              <th>Days Left</th>
                              <th>Status</th>
                            </tr>
                          </thead>
                          <tbody>
                            %s
                          </tbody>
                        </table>
                    
                        <p><strong>⚠️ Note:</strong> Please renew your certificate(s) promptly to avoid service disruption.</p>
                    
                        <p class="footer">
                          This is an automated alert. If you have already renewed, please disregard. &copy; Ndhuvi Trading Enterprise.
                        </p>
                      </div>
                    </body>
                    </html>
                    """, user, 3, tableRows.toString());

            String subject = "CERTIFICATE EXPIRATION NOTIFICATION";

            try {
                sendGridEmailService.sendEmailWithHtml(user.getEmail(), subject, message); // Ensure this supports HTML content
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
