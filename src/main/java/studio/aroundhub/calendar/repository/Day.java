package studio.aroundhub.calendar.repository;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import studio.aroundhub.member.repository.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "day")
@Getter
@Setter
public class Day {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_id", nullable = false)
    private Long id;

    @Column(name = "date", nullable = false)
    public LocalDate date;

    @Column(name = "daily_wage")
    private double dailyWage = 0;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Workplace> workplaces = new ArrayList<>();
}
