package studio.aroundhub.member.controller.request;

import lombok.Getter;

@Getter
public class ResetRequest {
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String loginId;
    private String password;
    private String confirmPassword;
    private String language;
}
