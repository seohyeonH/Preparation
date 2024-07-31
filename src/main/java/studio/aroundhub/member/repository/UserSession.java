package studio.aroundhub.member.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserSession {
    @Id
    private String id;
    private String state;
    private String nationality;
    private String userName;
    private String position;
    private String respondentName;
    private String respondentContact;
    private String companyName;
    private String companyAddress;
    private String wagePeriod;
    private String wageAmount;

    // 긴 문자열 -> 파일로 저장
    private String reason;
    private String demand;
    private String complete;
}
