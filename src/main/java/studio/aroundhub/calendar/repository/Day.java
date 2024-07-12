package studio.aroundhub.calendar.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "day")
@Getter
public class Day {
    @Id
    @Setter
    @Column(name = "day_id")
    private int id;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "month")
    private Calendar calendar;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Workplace> workplaces;
}