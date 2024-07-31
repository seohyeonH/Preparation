package studio.aroundhub.member.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studio.aroundhub.member.dto.LoginRequest;
import studio.aroundhub.member.dto.SignUpRequest;
import studio.aroundhub.member.dto.TemporaryResponse;
import studio.aroundhub.member.repository.User;
import studio.aroundhub.member.repository.UserRepository;
import studio.aroundhub.member.service.UserGalleryService;
import studio.aroundhub.member.service.UserService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final TemporaryResponse temporaryResponse;
    private final UserGalleryService userGalleryService;

    @GetMapping("/")
    public String hello() {
        return "Hello Ba;Da!";
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
    public ResponseEntity<?> addMember(@RequestBody SignUpRequest signUpRequest) {
        try {
            /* String country = temporaryResponse.getCountry(session.getId());
            String language = temporaryResponse.getLanguage(session.getId());

            signUpRequest.setCountry(country);
            signUpRequest.setLanguage(language);
            */
            userService.signUp(signUpRequest);
            //temporaryResponse.remoteCountryAndLanguage(session.getId());

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
    public ResponseEntity<String> login(@RequestParam(name = "Id") String Id, @RequestParam(name = "password") String password, LoginRequest loginRequest, HttpSession session) throws IllegalAccessException {
        return ResponseEntity.ok(userService.login(Id, password));
    }

    /**
     * 아이디 찾기
     * @return 회원의 loginId
     */
    @GetMapping("/find-loginId")
    public String findLoginId(@RequestParam(name = "firstName") String firstname, @RequestParam(name = "lastName") String lastname, @RequestParam(name = "phoneNumber") String phoneNumber) {
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
    public ResponseEntity<?> changeLanguage(@PathVariable Long user_id, @RequestParam String newLanguage){
        userService.changeLanguage(user_id, newLanguage);
        return ResponseEntity.ok("{}");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", e.getMessage()));
    }

    @GetMapping("/users0")
    public ResponseEntity<List<Map<String, Object>>> getUsers(@RequestParam(name = "date") String date) {
        List<Map<String, Object>> users = userService.getUsers(date);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users1")
    public ResponseEntity<List<Map<String, Object>>> getUsersList(@RequestParam(name = "date") String date) {
        List<Map<String, Object>> users = userService.getUsersList(date);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/save")
    public ResponseEntity<String> makeUserGallery(@RequestBody Map<String, Object> payload) throws IOException {
        userGalleryService.makeUserGallery((String) payload.get("userId"), (String) payload.get("imageUrl"));
            return ResponseEntity.ok().build();
    }

    @GetMapping("/save")
    public ResponseEntity<List<String>> getUserGallery(@RequestParam(name = "userId") String userId) {
        List<String> gallery = null;
        try {
            gallery = userGalleryService.getUserGallery(userId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(gallery);
    }
}
