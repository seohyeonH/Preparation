package studio.aroundhub.calendar.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "day")
@Getter
@Setter
public class Day {
    @Id
    @Setter
    @Column(name = "day_id")
    private int id;

    @Column(name = "date")
    public LocalDate date;

    // 월별 임금
    @Column(name = "monthly_wage")
    public long monthly_wage;


    // form 'calendar -> parent table'
    @ManyToOne
    @JoinColumn(name = "month")
    private Calendar calendar;

    // form 'workplace -> child table'
    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Workplace> workplaces;
}