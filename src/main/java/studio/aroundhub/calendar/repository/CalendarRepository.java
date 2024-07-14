package studio.aroundhub.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studio.aroundhub.member.repository.User;

import java.time.Month;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Integer> {
    List<Calendar> findByUserId(Long user_id);

    Optional<Calendar> findByUserIdAndMonth(User user, Month month);

    List<Calendar> findAllById(Long userId);
}
