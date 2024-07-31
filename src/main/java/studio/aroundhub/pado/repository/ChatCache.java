package studio.aroundhub.pado.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ChatCache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String prompt;

    @Lob
    @Column(nullable = false)
    private String response;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
