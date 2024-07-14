package studio.aroundhub.member.controller;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import studio.aroundhub.member.service.SMSService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ba;da/sms")
public class SMSController {
    @Autowired
    private final SMSService smsService;

    @PostMapping("/send-VerificationCode")
    public SingleMessageSentResponse sendVerification(@RequestParam String phoneNumber) {
        return smsService.sendVerificationCode(phoneNumber);
    }

    @PostMapping("/verify-Code")
    public boolean verifyCode(@RequestParam String phoneNumber, String Code) {
        // 유효시간이 지난 코드는 제외
        smsService.deleteVerificationCode();
        return smsService.verifyCode(phoneNumber, Code);
    }
}