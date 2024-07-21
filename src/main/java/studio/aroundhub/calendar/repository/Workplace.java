package studio.aroundhub.calendar.repository;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "workplace")
@Getter
@Setter
public class Workplace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workplace_id", nullable = false)
    private Long workplace_id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "breaktime", nullable = false)
    private int breaktime;

    @Column(name = "nightbreak", nullable = false)
    private int nightbreak;

    @Column(name = "wage", nullable = false)
    private double wage;

    @Column(name = "start", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "final", nullable = false)
    private LocalDateTime finalTime;
    
    @Column(name = "calculatemin", nullable = false)
    private boolean calculatemin = false;

    // form 'day -> parent table'
    @ManyToOne
    @JoinColumn(name = "day_id", nullable = false)
    private Day date;

    @Column(name = "today_pay")
    public double todayPay;

    public Workplace(Long workplace_id, String name, String label, String type, int breaktime, int nightbreak, LocalDateTime startTime, LocalDateTime finalTime, double wage, Day date) {
        this.workplace_id = workplace_id;
        this.name = name;
        this.label = label;
        this.type = type;
        this.startTime = startTime;
        this.finalTime = finalTime;
        this.breaktime = breaktime;
        this.nightbreak = nightbreak;
        this.wage = wage;
        this.date = date;
    }
}