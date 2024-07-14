package studio.aroundhub.calendar.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import studio.aroundhub.member.repository.User;

import java.time.Month;
import java.util.List;

@Entity
@Table(name = "calendar")
@Getter
@Setter
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_id")
    private int id;

    @Column(name = "month")
    public Month month;

    // form 'user -> parent table'
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // form 'day -> child table'
    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Day> days;
}
