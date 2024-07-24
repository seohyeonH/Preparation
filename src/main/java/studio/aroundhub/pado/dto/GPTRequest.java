package studio.aroundhub.pado.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GPTRequest {
    private String model;
    private List<Message> messages;

    public GPTRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }
}
