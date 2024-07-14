package studio.aroundhub.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {
}
