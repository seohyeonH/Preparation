package studio.aroundhub.member.service;

import com.neovisionaries.i18n.CountryCode;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {
    public List<String> getAllCountries() {
        return Arrays.stream(CountryCode.values())
                .map(CountryCode::getName)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     *  ISO 3166 국가 코드 데이터를 제공하는 라이브러리 사용해서 국가 끌어옴
     */
}