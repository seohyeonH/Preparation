package studio.aroundhub.member.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studio.aroundhub.member.controller.request.LoginRequest;
import studio.aroundhub.member.controller.request.SignUpRequest;
import studio.aroundhub.member.controller.response.TemporaryResponse;
import studio.aroundhub.member.repository.User;
import studio.aroundhub.member.repository.UserRepository;
import studio.aroundhub.member.service.UserService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final TemporaryResponse temporaryResponse;
    private final UserRepository userRepository;

    @GetMapping("/")
    public String hello() {
        return "Hello Ba;Da!";
    }

    /**
     * user 찾기
     * user_id로 사용자 정보를 조회하고 반환하는 로직 -> calendar에 필요
     * @param user_id database 내의 user 자체의 id
     * @return user의 정보
     */
    @GetMapping("/{user_id}")
    public ResponseEntity<User> getUserById(@PathVariable Long user_id) {
        User user = userRepository.findById(user_id).orElse(null);
        return ResponseEntity.ok(user);
    }

    /**
     * 회원 목록 조회 <br>
     * @return List<Member>
     */
    @GetMapping("/userlist")
    public ResponseEntity<?> getUserListTest() {
        return ResponseEntity.ok(userService.showAllUser());
    }

    /**
     * 국가 설정
     * @param payload 선택한 국가 정보
     */
    @PostMapping("/select_country")
    public ResponseEntity<String> selectCountry(@RequestBody Map<String, Object> payload, HttpSession session){
        String countryName = (String) payload.get("countryName");
        temporaryResponse.saveCountry(session.getId(), countryName);
        return ResponseEntity.ok("Selected country: " + countryName);
    }

    /** 언어 설정
     * @param payload 선택한 언어 정보
     */
    @PostMapping("/select_language")
    public ResponseEntity<String> selectLanguage(@RequestBody Map<String, Object> payload, HttpSession session){
        String language = (String) payload.get("language");
        temporaryResponse.saveLanguage(session.getId(), language);
        return ResponseEntity.ok("Selected country: " + language);
    }

    /**
     * 회원가입
     * @param signUpRequest 회원가입 요청 정보
     */
    @PostMapping("/signup")
    public ResponseEntity<?> addMember(@RequestBody SignUpRequest signUpRequest, HttpSession session) {
        try {
            String country = temporaryResponse.getCountry(session.getId());
            String language = temporaryResponse.getLanguage(session.getId());

            signUpRequest.setCountry(country);
            signUpRequest.setLanguage(language);

            userService.signUp(signUpRequest);

            temporaryResponse.remoteCountryAndLanguage(session.getId());

            return ResponseEntity.ok("[ User Info ]");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * 로그인
     * @return 로그인한 회원 정보
     */
    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpSession session) throws IllegalAccessException {
        return ResponseEntity.ok(userService.login(loginRequest, session));
    }

    /**
     * 아이디 찾기
     * @return 회원의 loginId
     */
    @GetMapping("/find-loginId")
    public String findLoginId(@RequestParam String firstname, @RequestParam String lastname,
                                                    @RequestParam String phoneNumber) {
        return userService.findLoginId(firstname, lastname, phoneNumber);
    }

    /**
     * 비밀번호 찾기
     */
    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestParam String loginId, @RequestParam String phoneNumber,
                                           @RequestParam String newPassword, @RequestParam String confirmPassword) {
        userService.findPassword(loginId, phoneNumber, newPassword, confirmPassword);
        return ResponseEntity.ok("{}");
    }

    /**
     * 비밀번호 변경
     */
    @PostMapping("/{user_id}/changePassword")
    public ResponseEntity<?> changePassword(@PathVariable Long user_id, @RequestParam String currentPassword,
                                            @RequestParam String newPassword, @RequestParam String confirmPassword){
        userService.changePassword(user_id, currentPassword, newPassword, confirmPassword);
        return ResponseEntity.ok("{}");
    }

    /**
    * 언어 변경
     */
    @PostMapping("/{user_id}/changeLanguage")
    public ResponseEntity<?> changeLangugage(@PathVariable Long user_id, @RequestParam String newLanguage){
        userService.changeLanguage(user_id, newLanguage);
        return ResponseEntity.ok("{}");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", e.getMessage()));
    }
}
