package studio.aroundhub.member.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 회원 확인
    @NotNull
    Optional<User> findById(@NotNull Long user_id);
    Optional<User> findByLoginIdAndPassword(String loginId, String password);

    // 아이디 중복 확인 + 비밀번호 찾기
    Optional<User> findByLoginId(String loginId);

    // 아이디 & 비밀번호 찾기
    Optional<User> findByFirstnameAndLastnameAndPhoneNumber(String firstname, String lastname, String phoneNumber);
    Optional<User> findByLoginIdAndPhoneNumber(String LoginId, String phoneNumber);

    // 비밀번호 변경
    Optional<User> findByPassword(String password);

    // 탈퇴
    Optional<User> deleteByLoginId(String loginId);
}
