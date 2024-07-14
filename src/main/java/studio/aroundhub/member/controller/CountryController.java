package studio.aroundhub.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studio.aroundhub.member.service.CountryService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ba;da")
public class CountryController {
    private final CountryService countryService;

    @GetMapping("/countries")
    public List<String> getAllCountries() {
        return countryService.getAllCountries();
    }
}
