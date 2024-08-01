package studio.aroundhub.pado.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studio.aroundhub.pado.service.*;

import java.io.File;
import java.io.IOException;
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

    // 진정서 작성
    @GetMapping("/pado/write")
    public ResponseEntity<?> writeDocument(@RequestParam(name = "userId") String userId, @RequestParam(name = "prompt") String message) throws IOException {
        Object result = openAIService.writeDocument(userId, message);

        if (result instanceof String) return ResponseEntity.ok(result);
        else if (result instanceof File file) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            return new ResponseEntity<>(new FileSystemResource(file), headers, HttpStatus.OK);
        }
        else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
    }

    // 환율
    @GetMapping("/exchange")
    public ResponseEntity<Map<String, Object>> getExchange(@RequestParam("userId") String userId, @RequestParam("date") String date) throws IOException {
        Map<String, Object> res = exchangeService.getExchange(userId, date);
        return ResponseEntity.ok(res);
    }
}