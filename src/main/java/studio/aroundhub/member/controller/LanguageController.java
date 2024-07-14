package studio.aroundhub.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studio.aroundhub.member.repository.User;
import studio.aroundhub.member.repository.UserRepository;
import studio.aroundhub.member.service.LanguageService;
import studio.aroundhub.member.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/ba;da")
public class LanguageController {
    private final LanguageService languageService;
    private final UserRepository userRepository;

    public LanguageController(LanguageService languageService, UserRepository userRepository) {
        this.languageService = languageService;
        this.userRepository = userRepository;
    }

    @GetMapping("/languages")
    public List<String> getAllLanguages() {
        return languageService.getAllLanguages();
    }

    @PostMapping("/{user_id}/changeLanguage")
    public ResponseEntity<?> changeLanguage(@RequestParam String loginId, @RequestParam String newlanguage){
        languageService.changeLanguage(loginId, newlanguage);
        return ResponseEntity.ok("{}");
    }
}
