package studio.aroundhub.member.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, String>{
    @NotNull
    Optional<UserSession> findById(@NotNull String id);
}
