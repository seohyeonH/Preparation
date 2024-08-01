package studio.aroundhub.pado.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studio.aroundhub.pado.service.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BotController {
    private final ChatCacheService chatCacheService;
    private final ExchangeService exchangeService;
    private final OpenAIService openAIService;

    // 챗봇 사용
    @GetMapping("/pado")
    public String chat(@RequestParam(name = "userId") String userId, @RequestParam(name = "prompt") String prompt) throws IOException {
        prompt = "Please answer this question based on the context in South Korea: " + prompt;

        return chatCacheService.getInfo(prompt, false);
    }

    // 진정서
    @GetMapping("/pado/write")
    public ResponseEntity<List<String>> writeDocument(@RequestParam(name = "userId") String userId, @RequestParam(name = "prompt") String message) throws Exception {
        List<String> result = openAIService.writeDocument(userId, message);

        return ResponseEntity.ok(result);
    }

    // 환율
    @GetMapping("/exchange")
    public ResponseEntity<Map<String, Object>> getExchange(@RequestParam("userId") String userId, @RequestParam("date") String date) throws IOException {
        Map<String, Object> res = exchangeService.getExchange(userId, date);
        return ResponseEntity.ok(res);
    }
}