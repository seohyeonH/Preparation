package studio.aroundhub.member.repository;

import jakarta.persistence.*;
import lombok.*;
import studio.aroundhub.calendar.repository.Calendar;

import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Setter
    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "month", nullable = false)
    private int month;

    @Column(name = "day", nullable = false)
    private int day;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "loginid", nullable = false)
    private String loginId;

    @Setter
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "user")
    private List<Calendar> calendars;

    @Builder
    public User(String firstname, String lastname, int month, int day, int year, String loginId, String password, String gender, String phoneNumber, String country, String language) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.month = month;
        this.day = day;
        this.year = year;
        this.loginId = loginId;
        this.password = password;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.language = language;
    }
}
