package studio.aroundhub.calendar.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {
}
