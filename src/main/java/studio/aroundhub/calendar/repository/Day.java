package studio.aroundhub.calendar.repository;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import studio.aroundhub.member.repository.User;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "day")
@Getter
@Setter
public class Day {
    @Id
    @Column(name = "day_id", nullable = false)
    private Long id;

    @Column(name = "date", nullable = false)
    public LocalDate date;

    // 일별 임금
    @Column(name = "daily_wage")
    private double dailyWage = 0;

    // form 'user -> parent table'
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // form 'workplace -> child table'
    @OneToMany(mappedBy = "date", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Workplace> workplaces;
}