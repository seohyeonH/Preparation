package studio.aroundhub.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
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
    private final VerificationRepository verificationRepository;
    private DefaultMessageService messageService;
    private final String fromNumber = "01099753084";

    @Transactional
    public SingleMessageSentResponse sendVerificationCode(String to) {
        String verificationCode = generateCode();

        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(to);
        message.setText("[Ba;da] 아래의 인증번호를 입력해주세요\n" + verificationCode);

        SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);
        SingleMessageSentResponse response = messageService.sendOne(request);

        if (response == null) throw new RuntimeException("SMS 전송에 실패했습니다.");

        verificationRepository.save(new Verification(to, verificationCode));
        return response;
    }

    private String generateCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt((int) Math.pow(10, 6)));
    }

    @Transactional
    public boolean verifyCode(String phoneNumber, String Code) {
        Verification latestCode = verificationRepository.findTopByPhoneNumberOrderByCreatedAtDesc(phoneNumber);

        if (latestCode != null) {
            LocalDateTime now = LocalDateTime.now();

            if (ChronoUnit.MINUTES.between(latestCode.getCreatedAt(), now) <= 5) return latestCode.getCode().equals(Code);
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
