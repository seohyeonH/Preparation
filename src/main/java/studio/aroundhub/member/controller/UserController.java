package studio.aroundhub.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studio.aroundhub.member.controller.request.LoginRequest;
import studio.aroundhub.member.controller.request.SignUpRequest;
import studio.aroundhub.member.controller.response.UserResponse;
import studio.aroundhub.member.repository.User;
import studio.aroundhub.member.repository.UserRepository;
import studio.aroundhub.member.service.SMSService;
import studio.aroundhub.member.service.UserService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private SMSService smsService;

    @GetMapping("/data")
    public ResponseEntity<String> getData() {
        return ResponseEntity.ok("Hello from Spring Boot");
    }

    @PostMapping("/data")
    public ResponseEntity<String> postData(@RequestBody UserResponse data) {
        // 데이터 처리 로직
        return ResponseEntity.ok("Data received");
    }

    @GetMapping("/")
    public String hello() {
        return "Hello Around Hub Studio!";
    }

    /** 회원 목록 조회
     * @return List<MemberResponse>
     */
    @GetMapping("/user-response")
    public ResponseEntity getUserList() {
        return ResponseEntity.ok(userService.findAllMember());
    }

    /** 회원 목록 조회 <br>
     * Entity를 그대로 반환하는 경우
     * @return List<Member>
     */
    @GetMapping("/members-test")
    public ResponseEntity getUserListTest() {
        return ResponseEntity.ok(userService.findAllMemberTest());
    }

    /** 회원가입
     * @param signUpRequest 회원가입 요청 정보
     * @return 회원가입 성공 시 빈 JSON 객체 반환
     */
    @PostMapping("/signup")
    public ResponseEntity<?> addMember(@RequestBody SignUpRequest signUpRequest) {
        try {
            userService.signUp(signUpRequest);
            return ResponseEntity.ok().body(Collections.singletonMap("message", "Signup successful"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    /** 로그인
     * @param loginRequest 아이디, 비밀번호
     * @return 로그인한 회원 정보
     * @throws IllegalAccessException 로그인 실패 시
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) throws IllegalAccessException {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    /** 아이디 찾기
     * @param firstname 이름
     * @param lastname 성
     * @param phoneNumber 전화번호
     * @return 사용자의 정보
     */
    @GetMapping("/find-loginId")
    public ResponseEntity<UserResponse> findLoginId(@RequestParam String firstname, @RequestParam String lastname,
                                                    @RequestParam String phoneNumber) {
        UserResponse userResponse = userService.findLoginId(firstname, lastname, phoneNumber);
        return ResponseEntity.ok(userResponse);
    }

    /** 비밀번호 찾기
     * @return 비밀번호 재설정 성공 시 빈 JSON 객체 반환
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String loginId, @RequestParam String phoneNumber,
                                           @RequestParam String newPassword, @RequestParam String confirmPassword) {
        userService.findPassword(loginId, phoneNumber, newPassword, confirmPassword);
        return ResponseEntity.ok("{}");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        // 예시: 오류 발생 시 명확한 오류 메시지 반환
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", e.getMessage()));
    }
}
