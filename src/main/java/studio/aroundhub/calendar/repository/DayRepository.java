package studio.aroundhub.calendar.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {
    @NotNull
    Optional<Day> findById(@NotNull Long Id);
}
