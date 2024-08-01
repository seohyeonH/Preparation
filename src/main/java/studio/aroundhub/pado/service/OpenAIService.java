package studio.aroundhub.pado.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studio.aroundhub.member.repository.UserSession;
import studio.aroundhub.member.repository.UserSessionRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAIService {
    private final PdfService pdfService;
    private final ChatCacheService chatCacheService;
    private final FileStorageService fileStorageService;
    private final FileUploadService fileUploadService;
    private static final String WAITING_FOR_AGREEMENT = "WAITING_FOR_AGREEMENT";
    private static final String WAITING_FOR_USER_INFO = "WAITING_FOR_USER_INFO";
    private static final String WAITING_FOR_RESPONDENT_INFO = "WAITING_FOR_RESPONDENT_INFO";
    private static final String WAITING_FOR_WAGE_PERIOD = "WAITING_FOR_WAGE_PERIOD";
    private static final String WAITING_FOR_REASON = "WAITING_FOR_REASON";
    private static final String WAITING_FOR_DEMAND = "WAITING_FOR_DEMAND";
    private static final String WAITING_FOR_CONFIRMATION = "WAITING_FOR_CONFIRMATION";
    private final UserSessionRepository userSessionRepository;

    public List<String> writeDocument(String userId, String message) throws Exception {
        UserSession userSession = userSessionRepository.findById(userId).orElseGet(() -> {
            UserSession newUser = new UserSession();
            newUser.setId(userId);
            newUser.setState(WAITING_FOR_AGREEMENT);
            userSessionRepository.save(newUser);
            return newUser;
        });

        String responseText;
        List<String> ans = new ArrayList<>();
        switch (userSession.getState()) {
            // 동의 구하기
            case WAITING_FOR_AGREEMENT:
                if (message.equalsIgnoreCase("Yes")) {
                    responseText = "Please enter your nationality, name, foreign-registration number, phone number, and address to write the petition.\n" +
                            "(Example: USA, SeohyeonHong, 00000000, 01099753084, Ansan)\n";
                    userSession.setState(WAITING_FOR_USER_INFO);
                } else responseText = "Please confirm if you want to proceed with the service.";
                break;

            // 사용자 정보
            case WAITING_FOR_USER_INFO:
                String[] details = message.split(", ");
                if (details.length > 4) {
                    userSession.setNationality(details[0]);
                    userSession.setUserName(details[1]);
                    userSession.setForeignNumber(details[2]);
                    userSession.setPhoneNumber(details[3]);

                    String address = String.join(" ", Arrays.copyOfRange(details, 3, details.length));
                    userSession.setAddress(address);
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
                    String companyAddress = String.join(" ", Arrays.copyOfRange(respondentInfo, 3, respondentInfo.length));
                    userSession.setCompanyAddress(companyAddress);

                    responseText = "Thank you! Now, Please provide the wage amount and period in the following format. Make sure to clearly specify the wage amount and the period.\n(Example: 700000 KRW, from May 20 to June 20)";
                    userSession.setState(WAITING_FOR_WAGE_PERIOD);
                } else responseText = "Please provide the respondent's name, contact number, exact company name, and address in the format: Name, Contact Number, Company Name, Address.";
                break;

            // 임금 정보
            case WAITING_FOR_WAGE_PERIOD:
                String[] periodAndAmount = message.split(", ");
                if (periodAndAmount.length == 2) {
                    userSession.setWageAmount(periodAndAmount[0].trim());
                    userSession.setWagePeriod(periodAndAmount[1].trim());
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
                        "Information for Writing a Wage Arrears Complaint Letter:\n" +
                        "\n" +
                        "1. Petitioner: %s\n" +
                        "2. Nationality: %s\n" +
                        "3. Foreign Registration Number: %s\n" +
                        "4. Address: %s\n" +
                        "5. Phone Number: %s\n\n" +
                        "6. Respondent's Name: %s\n" +
                        "7. Respondent's Phone Number: %s\n" +
                        "8. Company Name: %s\n" +
                        "9. Company Address: %s\n\n" +
                        "10. Arrears Amount: %s\n" +
                        "11. Period of Arrears: %s\n\n" +
                        "12. Background and Reasons for Wage Arrears" +
                        ": %s\n\n" +
                        "13. Demands in the Complaint Letter" +
                        ": %s\n",
                        userSession.getUserName(), userSession.getNationality(), userSession.getForeignNumber(), userSession.getAddress(), userSession.getPhoneNumber(),
                        userSession.getRespondentName(), userSession.getRespondentContact(), userSession.getCompanyName(), userSession.getCompanyAddress(),
                        userSession.getWageAmount(), userSession.getWagePeriod(), fileStorageService.loadResponseFromFile(userSession.getReason()),  fileStorageService.loadResponseFromFile(userSession.getDemand())))
                );
                responseText = "Thank you for participating in the consultation. Below is a summary of the consultation. If there are any errors, please let us know. If everything is correct, please type 'None'.";
                userSession.setState(WAITING_FOR_CONFIRMATION);
                break;

            // 요약 정보 확인
            case WAITING_FOR_CONFIRMATION:
                if (message.equalsIgnoreCase("None")) {
                    String check = fileStorageService.loadResponseFromFile(userSession.getComplete());

                    String response = "This is the complaint letter prepared based on the consultation details. Thank you.\n\n" + check;
                    String aiResponse = chatCacheService.getInfo(check + "을 바탕으로 임금 체불 진정서를 논리에 맞게 한국어로 작성해줘. 대신 제목은 없이 본문만 있으면 돼.", false);
                    File file = pdfService.generatePdf(aiResponse, userSession);

                    ans.add(response);
                    fileUploadService.uploadFile(file);
                    responseText = fileUploadService.generateFileUrl(file.getName());
                } else responseText = "If everything is correct, please type 'None'.";
                break;

            // 그 외의 경우
            default:
                responseText = "I’m sorry, I didn’t understand that. Could you please provide more details?";
                userSession.setState(WAITING_FOR_AGREEMENT);
                break;
        }

        ans.add(responseText);
        userSessionRepository.save(userSession);
        return ans;
    }
}