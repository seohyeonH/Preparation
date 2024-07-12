package studio.aroundhub.member.service;

import lombok.RequiredArgsConstructor;
import okhttp3.Cookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.aroundhub.member.controller.request.LoginRequest;
import studio.aroundhub.member.controller.request.ResetRequest;
import studio.aroundhub.member.controller.request.SignUpRequest;
import studio.aroundhub.member.controller.response.UserResponse;
import studio.aroundhub.member.repository.User;
import studio.aroundhub.member.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<User> findAllMemberTest() {
        return userRepository.findAll();
    }

    public List<UserResponse> findAllMember() {
        return userRepository.findAll().stream()
                .map(user ->
                        new UserResponse(
                                user.getId(),
                                user.getFirstname(),
                                user.getLastname(),
                                user.getMonth(),
                                user.getDay(),
                                user.getYear(),
                                user.getGender(),
                                user.getPhoneNumber().replaceAll("-",""),
                                user.getCountry(),
                                user.getLanguage()
                        )).toList();
    }

    // 회원가입
    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        // 비밀번호 일치 확인
        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Check your password");
        }

        // 비밀번호 길이 확인
        if (signUpRequest.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        // 아이디 중복 확인
        userRepository.findByLoginId(signUpRequest.getLoginId())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("The ID is unavailable.");
                });

        // 앞전에 받아둔 국가와 언어를 받기 위한 변수 설정
        String country = signUpRequest.getCountry();
        String language = signUpRequest.getLanguage();

        User user = User.builder()
                .firstname(signUpRequest.getFirstname())
                .lastname(signUpRequest.getLastname())
                .month(signUpRequest.getMonth())
                .day(signUpRequest.getDay())
                .year(signUpRequest.getYear())
                .phoneNumber(signUpRequest.getPhoneNumber().replaceAll("-",""))
                .gender(signUpRequest.getGender())
                .loginId(signUpRequest.getLoginId())
                .password(passwordEncoder.encode(signUpRequest.getPassword())) // 비밀번호 암호화
                .language(language)
                .country(country)
                .build();

        userRepository.save(user);
    }

    @Transactional
    public UserResponse login(LoginRequest loginRequest) {
        // 로그인 아이디와 비밀번호로 사용자 찾기
        User user = userRepository.findByLoginIdAndPassword(loginRequest.getLoginId(), loginRequest.getPassword())
                .orElseThrow(() -> new IllegalArgumentException("Incorrect ID or Password. Please check again."));

        //Cookie cookieId = new Cookie("userId", String.valueOf(user.getId()));


        return new UserResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getDay(),
                user.getMonth(),
                user.getYear(),
                user.getPhoneNumber(),
                user.getGender(),
                user.getCountry(),
                user.getLanguage()
        );
    }

    public UserResponse findLoginId(String firstname, String lastname, String phoneNumber) {
        // 이름과 성, 전화번호로 사용자 찾기
        User user = userRepository.findByFirstnameAndLastnameAndPhoneNumber(firstname, lastname, phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("ID not found. Make sure your name and phone number are correct."));

        return new UserResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getDay(),
                user.getMonth(),
                user.getYear(),
                user.getPhoneNumber(),
                user.getGender(),
                user.getCountry(),
                user.getLanguage()
        );
    }

    @Transactional
    public void findPassword(String loginId, String phoneNumber, String newPassword, String confirmPassword) {
        // 아이디와 전화번호로 사용자 찾기
        User user = userRepository.findByLoginIdAndPhoneNumber(loginId, phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("멘트 조정 중"));

        // 비밀번호 길이 확인
        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        // 새로 설정한 비밀번호와 재확인 비밀번호가 다를 시
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Check your password.");
        }

        // 변경 내용 저장
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public UserResponse findUser(String loginId){
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));
        return new UserResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getMonth(),
                user.getDay(),
                user.getYear(),
                user.getGender(),
                user.getPhoneNumber(),
                user.getCountry(),
                user.getLanguage()
        );
    }
}
