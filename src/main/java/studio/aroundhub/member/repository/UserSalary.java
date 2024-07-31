package studio.aroundhub.member.repository;

import jakarta.persistence.*;
import lombok.*;

import java.time.Month;

@Entity
@Table(name = "user_salary")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class UserSalary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "login_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "month")
    private Month month;

    @Column(name = "salary")
    private int salary;

    @Column(name = "hour")
    private long hour;

    @Column(name = "minutes")
    private long minutes;
}
