package studio.aroundhub.pado.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studio.aroundhub.member.repository.UserSession;
import studio.aroundhub.member.repository.UserSessionRepository;
import studio.aroundhub.pado.service.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BotController {
    private final ChatCacheService chatCacheService;
    private final ExchangeService exchangeService;
    private final PdfService pdfService;
    private static final String START = "START";
    private static final String WAITING_FOR_AGREEMENT = "WAITING_FOR_AGREEMENT";
    private static final String WAITING_FOR_USER_INFO = "WAITING_FOR_USER_INFO";
    private static final String WAITING_FOR_RESPONDENT_INFO = "WAITING_FOR_RESPONDENT_INFO";
    private static final String WAITING_FOR_WAGE_PERIOD = "WAITING_FOR_WAGE_PERIOD";
    private static final String WAITING_FOR_REASON = "WAITING_FOR_REASON";
    private static final String WAITING_FOR_DEMAND = "WAITING_FOR_DEMAND";
    private static final String WAITING_FOR_CONFIRMATION = "WAITING_FOR_CONFIRMATION";
    private final UserSessionRepository userSessionRepository;
    private final FileStorageService fileStorageService;
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