package studio.aroundhub.calendar.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studio.aroundhub.member.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {
    @NotNull
    Optional<Day> findById(@NotNull Long Id);
    Optional<Day> findByUserAndDate(User user, LocalDate date);
}