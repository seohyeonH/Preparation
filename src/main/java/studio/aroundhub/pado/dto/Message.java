package studio.aroundhub.pado.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String role;
    private String content;
}
