package studio.aroundhub.member.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.aroundhub.calendar.repository.Day;
import studio.aroundhub.calendar.repository.DayRepository;
import studio.aroundhub.calendar.repository.WorkplaceRepository;
import studio.aroundhub.member.dto.LoginRequest;
import studio.aroundhub.member.dto.SignUpRequest;
import studio.aroundhub.member.repository.User;
import studio.aroundhub.member.repository.UserRepository;
import studio.aroundhub.member.repository.UserSalary;
import studio.aroundhub.member.repository.UserSalaryRepository;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserSalaryRepository userSalaryRepository;
    private final WorkplaceRepository workplaceRepository;
    private final DayRepository dayRepository;

    // 회원가입
    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        // 비밀번호 일치 확인
        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword()))
            throw new IllegalArgumentException("Check your password");

        // 비밀번호 길이 확인
        if (signUpRequest.getPassword().length() < 8)
            throw new IllegalArgumentException("Password must be at least 8 characters long");

        // 아이디 중복 확인
        userRepository.findByLoginId(signUpRequest.getLoginId())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("The ID is unavailable.");
                });

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
                .country(signUpRequest.getCountry())
                .language(signUpRequest.getLanguage())
                .build();

        userRepository.save(user);
    }

    @Transactional
    public String login(String loginId, String password) {
        // 로그인 아이디로 사용자 찾기
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect ID or Password. Please check again."));

        /* 로그인 유지 여부
        if(loginRequest.isKeepLogin()){
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(14 * 24 * 60 * 60);
        }
        user.setKeepLogin(loginRequest.isKeepLogin());*/

        // 해싱하여 저장해놓은 user의 비밀번호와 입력한 비밀번호가 일치한지 확인
        if (passwordEncoder.matches(password, user.getPassword()))
            return user.getLoginId();
        else
            throw new IllegalArgumentException("Incorrect ID or Password. Please check again.");
    }

    @Transactional
    public String findLoginId(String firstname, String lastname, String phoneNumber) {
        // 이름과 성, 전화번호로 사용자 찾기
        User user = userRepository.findByFirstnameAndLastnameAndPhoneNumber(firstname, lastname, phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("ID not found. Make sure your name and phone number are correct."));

        return user.getLoginId();
    }

    @Transactional
    public void findPassword(String loginId, String phoneNumber, String newPassword, String confirmPassword) {
        // 아이디와 전화번호로 사용자 찾기
        User user = userRepository.findByLoginIdAndPhoneNumber(loginId, phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("멘트 조정 중"));

        // 비밀번호 길이 확인
        if (newPassword.length() < 8)
            throw new IllegalArgumentException("Password must be at least 8 characters long");

        // 새로 설정한 비밀번호와 재확인 비밀번호가 다를 시
        if (!newPassword.equals(confirmPassword))
            throw new IllegalArgumentException("Check your password.");

        // 변경 내용 저장
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void changeLanguage(Long user_id, String newLanguage){
        // DB상 user의 id로 사용자 찾기
        User user = userRepository.findById(user_id).orElseThrow();

        // 변경내용 저장
        user.setLanguage(newLanguage);
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long user_id, String currentPassword, String newPassword, String confirmPassword) {
        // DB상 user의 id로 사용자 찾기
        User user = userRepository.findById(user_id).orElseThrow();

        // 현재 비밀번호와 user의 비밀번호가 다를 시,
        if (!passwordEncoder.matches(currentPassword, user.getPassword()))
            throw new IllegalArgumentException("Current password is incorrect.");

        // 현재 비밀번호와 새비밀번호가 같을 시,
        if (currentPassword.equals(newPassword))
            throw new IllegalArgumentException("New Password and Current Password are the same");

        // 새비밀번호의 길이가 7 이하일 시,
        if (newPassword.length() < 8)
            throw new IllegalArgumentException("Password must be at least 8 characters long");

        // 새비밀번호와 확인비밀번호가 다를 시,
        if (!newPassword.equals(confirmPassword))
            throw new IllegalArgumentException("어떻게 할지 고민 중");

        // 변경 내용 저장
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // 다른 유저 정보 패스
    public List<Map<String, Object>> getUsers(String date) {
        Month month = LocalDate.parse(date).getMonth();
        List<User> users = userRepository.findAll();
        if(users.size() <= 6) {
            return users.stream()
                    .map(user -> convertToMap(user, month))
                    .collect(Collectors.toList());
        }
        else {
            Collections.shuffle(users);
            List<User> randomUsers = users.subList(0, 6);
            return randomUsers.stream()
                    .map(user -> convertToMap(user, month))
                    .collect(Collectors.toList());
        }
    }

    public List<Map<String, Object>> getUsersList(String date) {
        Month month = LocalDate.parse(date).getMonth();
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> convertToMap(user, month))
                .collect(Collectors.toList());
    }

    private Map<String, Object> convertToMap(User user, Month month) {
        UserSalary salary = userSalaryRepository.findByUserAndMonth(user, month).orElse(null);
        if(salary == null) return Map.of();

        Day workplaceDate = dayRepository.findByUserAndDate(user, LocalDate.parse("2024-07-22")).orElse(null);
        if(workplaceDate == null) return Map.of();

        String type = workplaceDate.getWorkplaces().get(0).getType();
        LocalDate birth = LocalDate.of(user.getYear(), user.getMonth(), user.getDay());
        LocalDate now = LocalDate.now();
        int age = Period.between(birth, now).getYears();

        String gender = (user.getGender().equals("Female")) ? "♀" : "♂";
        return Map.of(
                "Gender", gender,
                "Age", age,
                "Work Type", type,
                "Monthly working hours", salary.getHour() + " H " + salary.getMinutes() + "M",
                "Total Wage", salary.getSalary() + " ₩"
        );
    }
}
