package studio.aroundhub.member.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import studio.aroundhub.calendar.repository.Day;
import java.util.ArrayList;
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

    @JsonProperty("loginId")
    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Setter
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Day> days = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserSalary> monthlySalary = new ArrayList<>();

    @Setter
    @Column(name = "gallery", nullable = true)
    private String gallery;

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