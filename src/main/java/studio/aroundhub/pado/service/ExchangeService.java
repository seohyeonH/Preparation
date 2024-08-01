package studio.aroundhub.pado.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import studio.aroundhub.member.repository.User;
import studio.aroundhub.member.repository.UserRepository;
import studio.aroundhub.member.repository.UserSalary;
import studio.aroundhub.member.repository.UserSalaryRepository;
import javax.net.ssl.*;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final ChatCacheService chatCacheService;
    private final UserRepository userRepository;
    private final UserSalaryRepository userSalaryRepository;
    @Value("${exchange.api.key}")
    private String apiKey;
    private static final String API_URL = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON";

    private void disableSslVerification() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public Double getRate(String date, String code) {
        String dataType = "AP01";
        Double rate = null;
        BufferedReader reader = null;
        HttpURLConnection con = null;
        JSONParser parser = new JSONParser();

        try {
            disableSslVerification();
            URL url = new URL(API_URL + "?authkey=" + apiKey + "&searchdate=" + date + "&data=" + dataType);
            System.out.println("Request URL: " + url);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setInstanceFollowRedirects(false);

            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder responseContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) responseContent.append(line);

            String response = responseContent.toString();
            System.out.println("Response Content: " + response);

            if (response.isEmpty()) throw new RuntimeException("Empty response from API");

            JSONArray rateList = (JSONArray) parser.parse(response);
            for (Object r : rateList) {
                JSONObject exchangeRateInfo = (JSONObject) r;
                if (exchangeRateInfo.get("cur_unit").equals(code)) {
                    NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                    rate = format.parse(exchangeRateInfo.get("deal_bas_r").toString()).doubleValue();
                    break;
                }
            }
        } catch (IOException | ParseException | java.text.ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get exchange rate", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) con.disconnect();
        }
        if (rate == null) rate = 1200.0;

        return rate;
    }

    @Transactional
    public Map<String, Object> getExchange(String loginId, String dateStr) throws IOException {
        String d = dateStr.replace("\"", "");
        LocalDate date = LocalDate.parse(d, DateTimeFormatter.ISO_LOCAL_DATE);
        Map<String, Object> data = new HashMap<>();

        User user = userRepository.findByLoginId(loginId).orElse(null);
        if (user == null) return Map.of();

        UserSalary salary = userSalaryRepository.findByUserAndMonth(user, date.getMonth()).orElse(null);
        if (salary == null) return Map.of();

        String prompt = user.getCountry() + "의 통화기호를 키워드로만 알려줘.";
        String code = chatCacheService.getInfo(prompt, true);
        double exchangeCost = salary.getSalary() / 1370.14;

        if (Objects.equals(code, "USD")) code = "$";
        else if (Objects.equals(code, "CNY")) code = "Y";
        else if (Objects.equals(code, "JPY")) code = "¥";
        else if (Objects.equals(code, "THB")) code = "฿";
        else if (Objects.equals(code, "UZS")) code = "UZS";

        data.put("monthlySalary", String.valueOf(salary.getSalary()));
        data.put("exchangeSalary", String.valueOf((float) exchangeCost));
        data.put("code", code);

        return data;
    }
}