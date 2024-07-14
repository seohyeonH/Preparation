package studio.aroundhub.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studio.aroundhub.member.service.LanguageService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ba;da")
public class LanguageController {
    private final LanguageService languageService;

    @GetMapping("/languages")
    public List<String> getAllLanguages() {
        return languageService.getAllLanguages();
    }

    @PostMapping("/{user_id}/changeLanguage")
    public ResponseEntity<?> changeLanguage(@PathVariable Long user_id, @RequestParam String newlanguage){
        languageService.changeLanguage(user_id, newlanguage);
        return ResponseEntity.ok("{}");
    }
}
