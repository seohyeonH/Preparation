package studio.aroundhub.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface VerificationRepository extends JpaRepository<Verification, String> {
    @Modifying
    @Query("DELETE FROM Verification v WHERE v.createdAt < :expirationTime")
    void deleteVerificationCodes(@Param("expirationTime") LocalDateTime expirationTime);

    @Query("SELECT v FROM Verification v WHERE v.phoneNumber = :phoneNumber ORDER BY v.createdAt DESC")
    Verification findTopByPhoneNumberOrderByCreatedAtDesc(@Param("phoneNumber") String phoneNumber);
}
