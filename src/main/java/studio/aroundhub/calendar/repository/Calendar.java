package studio.aroundhub.calendar.repository;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import studio.aroundhub.member.repository.User;

import java.time.Month;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "calendar")
@Getter
@Setter
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_id")
    private Long id;

    @Column(name = "month")
    public Month month;

    // form 'user -> parent table'
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // form 'day -> child table'
    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Day> days;

    public Calendar(Month month, Long id, User user, List<Day> days) {
        this.month = month;
        this.id = id;
        this.user = user;
        this.days = days;
    }
}
