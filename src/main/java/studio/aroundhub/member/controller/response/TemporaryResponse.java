package studio.aroundhub.member.controller.response;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class TemporaryResponse {
    private final Map<String, String> country = new HashMap<>();
    private final Map<String, String> language = new HashMap<>();

    public void saveCountry(String sessionId, String country){
        this.country.put(sessionId, country);
    }

    public void saveLanguage(String sessionId, String language){
        this.language.put(sessionId, language);
    }

    public String getCountry(String sessionId){
        return country.get(sessionId);
    }

    public String getLanguage(String sessionId){
        return language.get(sessionId);
    }

    public void remoteCountryAndLanguage(String sessionId){
        country.remove(sessionId);
        language.remove(sessionId);
    }
}
