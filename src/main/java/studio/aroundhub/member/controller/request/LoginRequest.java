package studio.aroundhub.member.controller.request;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String loginId;
    private String password;
}
