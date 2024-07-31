package studio.aroundhub.pado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatCacheRepository extends JpaRepository<ChatCache, Long> {
    ChatCache findByPrompt(String prompt);
}
