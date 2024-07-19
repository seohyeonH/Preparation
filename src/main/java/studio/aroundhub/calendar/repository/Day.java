package studio.aroundhub.calendar.repository;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "day")
@Getter
@Setter
public class Day {
    @Id
    @Column(name = "day_id")
    private Long id;

    @Column(name = "date")
    public LocalDate date;

    // 월별 임금
    @Column(name = "daily_wage")
    private double dailyWage = 0;

    // form 'calendar -> parent table'
    @ManyToOne
    @JoinColumn(name = "month")
    private Calendar calendar;

    // form 'workplace -> child table'
    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Workplace> workplaces;

    public Day(Long id, LocalDate date, long daily_wage, Calendar calendar, List<Workplace> workplaces) {
        this.id = id;
        this.date = date;
        this.dailyWage = daily_wage;
        this.calendar = calendar;
        this.workplaces = workplaces;
    }
}