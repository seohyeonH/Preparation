package studio.aroundhub.pado.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studio.aroundhub.member.repository.UserSession;
import studio.aroundhub.member.repository.UserSessionRepository;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OpenAIService {
    private final PdfService pdfService;
    private final ChatCacheService chatCacheService;
    private static final String START = "START";
    private static final String WAITING_FOR_AGREEMENT = "WAITING_FOR_AGREEMENT";
    private static final String WAITING_FOR_USER_INFO = "WAITING_FOR_USER_INFO";
    private static final String WAITING_FOR_RESPONDENT_INFO = "WAITING_FOR_RESPONDENT_INFO";
    private static final String WAITING_FOR_WAGE_PERIOD = "WAITING_FOR_WAGE_PERIOD";
    private static final String WAITING_FOR_REASON = "WAITING_FOR_REASON";
    private static final String WAITING_FOR_DEMAND = "WAITING_FOR_DEMAND";
    private static final String WAITING_FOR_CONFIRMATION = "WAITING_FOR_CONFIRMATION";
    private final UserSessionRepository userSessionRepository;
    private final FileStorageService fileStorageService;

    public Object writeDocument(String userId, String message) throws IOException {
        UserSession userSession = userSessionRepository.findById(userId).orElseGet(() -> {
            UserSession newUser = new UserSession();
            newUser.setId(userId);
            newUser.setState(START);
            userSessionRepository.save(newUser);
            return newUser;
        });

        String responseText;
        switch (userSession.getState()) {
            // 시작
            case START:
                responseText = "Hello! Our wage arrears complaint service automatically generates a complaint letter through a consultation with the user. Do you agree?";
                userSession.setState(WAITING_FOR_AGREEMENT);
                break;

            // 동의 구하기
            case WAITING_FOR_AGREEMENT:
                if (message.equalsIgnoreCase("Yes")) {
                    responseText = "Please provide your nationality, name, and position.";
                    userSession.setState(WAITING_FOR_USER_INFO);
                } else responseText = "Please confirm if you want to proceed with the service.";
                break;

            // 사용자 정보
            case WAITING_FOR_USER_INFO:
                String[] details = message.split(", ");
                if (details.length == 3) {
                    userSession.setNationality(details[0]);
                    userSession.setUserName(details[1]);
                    userSession.setPosition(details[2]);
                    responseText = "Thank you! Now, please enter the respondent’s name, contact information, exact company name, and address.";

                    userSession.setState(WAITING_FOR_RESPONDENT_INFO);
                } else responseText = "Please provide your nationality, name, and position in the format: Nationality, Name, Position.";
                break;

            // 피진정인 정보
            case WAITING_FOR_RESPONDENT_INFO:
                String[] respondentInfo = message.split(", ");
                if (respondentInfo.length > 3) {
                    userSession.setRespondentName(respondentInfo[0]);
                    userSession.setRespondentContact(respondentInfo[1]);
                    userSession.setCompanyName(respondentInfo[2]);
                    userSession.setCompanyAddress(String.join(" ", respondentInfo[3], respondentInfo[4], respondentInfo[5]));

                    responseText = "Thank you! Now, Please provide the wage amount and period in the following format. Make sure to clearly specify the wage amount and the period.\n(Example: 700000 KRW, from May 20 to June 20)";
                    userSession.setState(WAITING_FOR_WAGE_PERIOD);
                } else responseText = "Please provide the respondent's name, contact number, exact company name, and address in the format: Name, Contact Number, Company Name, Address.";
                break;

            // 임금 정보
            case WAITING_FOR_WAGE_PERIOD:
                String[] periodAndAmount = message.split(", ");
                if (periodAndAmount.length == 2) {
                    userSession.setWagePeriod(periodAndAmount[1].trim());
                    userSession.setWageAmount(periodAndAmount[0].trim());
                    responseText = "Thank you. Please describe the specific background and reasons for the wage arrears.";

                    userSession.setState(WAITING_FOR_REASON);
                } else responseText = "Please provide the period and amount in the format: Amount, Period.";
                break;

            // 배경 및 사유
            case WAITING_FOR_REASON:


                userSession.setReason(fileStorageService.saveResponseToFile(userSession.getId() + "reason", message));
                responseText = "What are your demands through this complaint letter?";
                userSession.setState(WAITING_FOR_DEMAND);
                break;

            // 요구사항
            case WAITING_FOR_DEMAND:
                userSession.setDemand(fileStorageService.saveResponseToFile(userSession.getId() + "demand", message));
                userSession.setComplete(fileStorageService.saveResponseToFile(userSession.getId(), String.format(
                        """
                                Information for Writing a Wage Arrears Complaint Letter:

                                1. Applicant Name: %s
                                2. Nationality: %s
                                3. Position: %s
                                4. Respondent's Name: %s
                                5. Respondent's Contact Information: %s
                                6. Company Name: %s
                                7. Company Address: %s
                                8. Arrears Amount: %s
                                9. Period of Arrears: %s
                                10. Background and Reasons for Wage Arrears:
                                 %s
                                11. Demands in the Complaint Letter: %s
                                """,
                        userSession.getUserName(), userSession.getNationality(), userSession.getPosition(),
                        userSession.getRespondentName(), userSession.getRespondentContact(), userSession.getCompanyName(), userSession.getCompanyAddress(),
                        userSession.getWageAmount(), userSession.getWagePeriod(), fileStorageService.loadResponseFromFile(userSession.getReason()),  fileStorageService.loadResponseFromFile(userSession.getDemand())))
                );
                responseText = "Thank you for participating in the consultation. Below is a summary of the consultation. If there are any errors, please let us know. If everything is correct, please type 'None'." + fileStorageService.loadResponseFromFile(userSession.getComplete());
                userSession.setState(WAITING_FOR_CONFIRMATION);
                break;

            // 요약 정보 확인
            case WAITING_FOR_CONFIRMATION:
                if (message.equalsIgnoreCase("None")) {
                    String check = fileStorageService.loadResponseFromFile(userSession.getComplete());
                    responseText = "This is the complaint letter prepared based on the consultation details. Thank you.\n\n" + check;

                    String aiResponse = chatCacheService.getInfo(check + "을 바탕으로 임금 체불 진정서를 논리에 맞게 작성하되, 한글로 완성해줘. " +
                            "그리고 너가 보내주는 진정서를 줄 바꿈을 기준으로 읽어서 pdf 파일로 만들 예정이라 감안해서 작성해주면 좋겠어.", false);
                    File file = pdfService.generatePdf(aiResponse);
                    userSessionRepository.save(userSession);
                    return file;
                } else responseText = "If everything is correct, please type 'None'.";
                break;

            // 그 외의 경우
            default:
                responseText = "I’m sorry, I didn’t understand that. Could you please provide more details?";
                userSession.setState(START);
                break;
        }
        userSessionRepository.save(userSession);
        return responseText;
    }
}