package studio.aroundhub.calendar.repository;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.sql.Time;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "workplace")
@Getter
@Setter
public class Workplace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workplace_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "label")
    private Color label;

    @Column(name = "type")
    private String type;

    @Column(name = "breaktime")
    private Time breaktime;

    @Column(name = "nightbreak")
    private Time nightbreak;

    @Column(name = "wage")
    private int wage;

    @Column(name = "Statutory-Leisure Pay")
    private boolean SLpay = false;
    
    @Column(name = "calculatemin")
    private boolean calculatemin = false;

    // form 'day -> parent table'
    @ManyToOne
    @JoinColumn(name = "date")
    private Day day;

    public Workplace(Long id, String name, Color label, String type, Time breaktime, Time nightbreak, int wage, boolean SLpay, boolean calculatemin, Day day) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.type = type;
        this.breaktime = breaktime;
        this.nightbreak = nightbreak;
        this.wage = wage;
        this.SLpay = SLpay;
        this.calculatemin = calculatemin;
        this.day = day;
    }
}