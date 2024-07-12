package studio.aroundhub.member.controller.response;

import lombok.Getter;

@Getter
public class UserResponse {
    Long userId;
    String firstname;
    String lastname;
    int month;
    int day;
    int year;
    String gender;
    String language;
    String phoneNumber;
    String country;

    public UserResponse(Long userId, String firstname, String lastname, int month, int day, int year, String gender, String phoneNumber, String country, String language) {
        this.userId = userId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.month = month;
        this.day = day;
        this.year = year;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.language = language;
        this.country = country;
    }
}
