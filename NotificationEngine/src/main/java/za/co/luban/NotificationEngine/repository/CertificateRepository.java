package za.co.luban.NotificationEngine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import za.co.luban.NotificationEngine.model.Certificates;

import java.util.Date;
import java.util.List;


@Repository
public interface CertificateRepository extends JpaRepository<Certificates, Long> {


    @Query(
            value =
                 """
                  Select * from Certificates c
                    where c.expiration_Date between ?1 and ?2
                 """,
          nativeQuery = true)
    List<Certificates> findCertificatesByExpirationDateBetween(Date currentDate, Date threeMonthsAfterCurrentDate);

}
