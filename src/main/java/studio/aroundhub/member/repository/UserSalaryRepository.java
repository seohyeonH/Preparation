package studio.aroundhub.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Month;
import java.util.Optional;

public interface UserSalaryRepository extends JpaRepository<UserSalary, Long> {
    Optional<UserSalary> findByUserAndMonth(User user, Month month);
}
