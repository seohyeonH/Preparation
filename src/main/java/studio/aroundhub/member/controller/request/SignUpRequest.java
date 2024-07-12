package studio.aroundhub.member.controller.request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String firstname;
    private String lastname;
    private int month;
    private int year;
    private int day;
    private String loginId;
    private String password;
    private String confirmPassword;
    private String certification_number;
    private String language;
    private String country;
    private String gender;
    private String phoneNumber;
}
