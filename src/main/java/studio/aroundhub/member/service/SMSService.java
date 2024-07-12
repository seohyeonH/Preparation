package studio.aroundhub.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import studio.aroundhub.member.repository.Verification;
import studio.aroundhub.member.repository.VerificationRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SMSService {
    private VerificationRepository verificationRepository;
    private DefaultMessageService messageService;

    private String fromNumber = "01099753084";

    @Transactional
    public SingleMessageSentResponse sendVerificationCode(String to) {
        // 인증번호 생성
        String verificationCode = generateCode();

        // 메세지 구성
        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(to);
        message.setText("[Ba;da] 아래의 인증번호를 입력해주세요\n" + verificationCode);

        // 메세지 전송
        SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);
        SingleMessageSentResponse response = messageService.sendOne(request);

        verificationRepository.save(new Verification(to, verificationCode));

        return response;
    }

    private String generateCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    @Transactional
    public boolean verifyCode(String phoneNumber, String Code) {
        Verification latestCode = verificationRepository.findTopByPhoneNumberOrderByCreatedAtDesc(phoneNumber);

        if (latestCode != null) {
            LocalDateTime now = LocalDateTime.now();
            if (ChronoUnit.MINUTES.between(latestCode.getCreatedAt(), now) <= 5) {
                return latestCode.getCode().equals(Code);
            }
        }
        return false;
    }

    @Scheduled(cron = "0 */5 * * * *") // 유효시간 5분
    @Transactional
    public void deleteVerificationCode() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(5);
        verificationRepository.deleteVerificationCodes(expirationTime);
    }
}
