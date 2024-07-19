package studio.aroundhub.calendar.repository;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "workplace")
@Getter
@Setter
public class Workplace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workplace_id")
    private Long workplace_id;

    @Column(name = "name")
    private String name;

    @Column(name = "label")
    private Color label;

    @Column(name = "type")
    private String type;

    @Column(name = "breaktime")
    private int breaktime;

    @Column(name = "nightbreak")
    private int nightbreak;

    @Column(name = "wage")
    private double wage;

    @Column(name = "start")
    private LocalDateTime startTime;

    @Column(name = "final")
    private LocalDateTime finalTime;
    
    @Column(name = "calculatemin")
    private boolean calculatemin = false;

    // form 'day -> parent table'
    @ManyToOne
    @JoinColumn(name = "date")
    private Day day;

    @Column(name = "today_pay")
    public double todayPay;

    public Workplace(Long workplace_id, String name, Color label, String type, int breaktime, int nightbreak, LocalDateTime startTime, LocalDateTime finalTime, double wage, Day day) {
        this.workplace_id = workplace_id;
        this.name = name;
        this.label = label;
        this.type = type;
        this.startTime = startTime;
        this.finalTime = finalTime;
        this.breaktime = breaktime;
        this.nightbreak = nightbreak;
        this.wage = wage;
        this.day = day;
    }
}