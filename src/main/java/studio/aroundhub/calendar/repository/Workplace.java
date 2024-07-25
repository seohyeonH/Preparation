package studio.aroundhub.calendar.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import studio.aroundhub.member.repository.User;

import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @JsonProperty("startTime")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @JsonProperty("endTime")
    @Column(name = "final_time", nullable = false)
    private LocalDateTime finalTime;

    @Column(name = "calculatemin", nullable = false)
    private boolean calculatemin = false;

    // form 'day -> parent table'
    @ManyToOne
    @JoinColumn(name = "day_id", nullable = false)
    private Day day;

    @Column(name = "today_pay")
    public double todayPay = 0;

    public Workplace(Long workplace_id, String name, String label, String type, int breaktime, int nightbreak, LocalDateTime startTime, LocalDateTime finalTime, double wage, Day day) {
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