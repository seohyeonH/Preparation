package studio.aroundhub.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.aroundhub.member.controller.request.SignUpRequest;
import studio.aroundhub.member.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private final UserRepository userRepository;

    public List<String> getAllLanguages() {
        return Arrays.asList("English", "Chinese", "Thai", "Vietnamese");
    }

    // 언어 설정 변경
    // 로그인 아이디로 통해 회원을 찾고, 정보가 있을 시 새롭게 받은 언어를 재설정함.
    // 정보가 없을 경우는 고려 X. 로그인 했다는 거 자체가 정보가 있다는 의미.
    @Transactional
    public void changeLanguage(Long user_id, String newLanguage) {
        userRepository.findById(user_id).ifPresent(user -> {
            user.setLanguage(newLanguage);
            userRepository.save(user);
        });
    }
}
