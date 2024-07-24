package studio.aroundhub.pado.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import studio.aroundhub.pado.dto.GPTRequest;
import studio.aroundhub.pado.dto.GPTResponse;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class BotController {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate restTemplate;

    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "prompt") String prompt){
        if (cache.containsKey(prompt)) {
            return cache.get(prompt);
        }

        GPTRequest request = new GPTRequest(model, prompt);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GPTRequest> entity = new HttpEntity<>(request, headers);

        try {
            GPTResponse response = restTemplate.postForObject(apiURL, entity, GPTResponse.class);
            String result = response.getChoices().get(0).getMessage().getContent();
            cache.put(prompt, result);
            return result;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 429) {
                return "Error: You have exceeded your current quota. Please check your plan and billing details. For more information, visit: https://platform.openai.com/docs/guides/error-codes/api-errors";
            } else {
                return "Error: " + e.getMessage();
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
