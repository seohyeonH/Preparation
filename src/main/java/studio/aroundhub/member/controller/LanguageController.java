package studio.aroundhub.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studio.aroundhub.member.service.LanguageService;
import java.util.List;

@RestController
@RequestMapping("/ba;da")
public class LanguageController {
    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping("/languages")
    public List<String> getAllLanguages() {
        return languageService.getAllLanguages();
    }
}
