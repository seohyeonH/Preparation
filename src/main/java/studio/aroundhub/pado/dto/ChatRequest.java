package studio.aroundhub.pado.dto;

import lombok.Data;
import java.util.List;

@Data
public class ChatRequest {
    private String model;
    private List<Message> messages;
    private int max_tokens;

    public ChatRequest(String model, String prompt, int max_tokens) {
        this.model = model;
        this.messages = List.of(new Message("user", prompt));
        this.max_tokens = max_tokens;
    }
}
