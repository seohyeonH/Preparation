package studio.aroundhub.pado.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import studio.aroundhub.pado.dto.ChatRequest;
import studio.aroundhub.pado.dto.ChatResponse;
import studio.aroundhub.pado.repository.ChatCache;
import studio.aroundhub.pado.repository.ChatCacheRepository;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatCacheService {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;
    private final RestTemplate restTemplate;
    private final FileStorageService fileStorageService;
    private final ChatCacheRepository chatCacheRepository;

    public String getInfo(String prompt, boolean save) throws IOException {
        String cachedResponse = getCachedResponse(prompt);
        if (cachedResponse != null) return fileStorageService.loadResponseFromFile(cachedResponse);

        ChatRequest chatRequest = new ChatRequest(model, prompt, 1000);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatRequest> entity = new HttpEntity<>(chatRequest, headers);

        ChatResponse chatResponse = restTemplate.postForObject(apiURL, entity, ChatResponse.class);
        if (chatResponse == null || chatResponse.getChoices().isEmpty())
            throw new RuntimeException("Invalid response from OpenAI");

        String result = chatResponse.getChoices().get(0).getMessage().getContent();

        if (save) {
            String filePath = fileStorageService.saveResponseToFile(prompt, result);
            saveCache(prompt, filePath);
        }

        return result;
    }

    public String getCachedResponse(String prompt) {
        ChatCache cache = chatCacheRepository.findByPrompt(prompt);
        return cache != null ? cache.getResponse() : null;
    }

    public void saveCache(String prompt, String response) {
        ChatCache cache = new ChatCache();
        cache.setPrompt(prompt);
        cache.setResponse(response);
        cache.setCreatedAt(LocalDateTime.now());
        chatCacheRepository.save(cache);
    }
}
