package studio.aroundhub.calendar.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Entity
@Table(name = "workplace")
@Getter
@Setter
public class Workplace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workplace_id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "breaktime")
    private Time breaktime;

    @Column(name = "nightbreak")
    private Time nightbreak;

    @Column(name = "wage")
    private int wage;

    @Column(name = "미정")
    private boolean none;
    
    @Column(name = "calculatemin")
    private boolean calculatemin;

    @ManyToOne
    @JoinColumn(name = "date")
    private Day day;
}