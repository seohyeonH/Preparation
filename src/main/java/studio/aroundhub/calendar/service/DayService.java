package studio.aroundhub.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.aroundhub.calendar.repository.*;
import studio.aroundhub.member.repository.*;

import java.time.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DayService {
    private final DayRepository dayRepository;
    private final WorkplaceRepository workplaceRepository;
    private final UserRepository userRepository;
    private final UserSalaryRepository userSalaryRepository;

    // 월별 라벨 색상 패스
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getLabels(String loginId, String startDate, String endDate) {
        User user = userRepository.findByLoginId(loginId).orElse(null);
        if (user == null) return List.of();

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<Day> days = user.getDays().stream()
                .filter(day -> !day.getDate().isBefore(start) && !day.getDate().isAfter(end))
                .toList();

        List<Map<String, Object>> labels = new ArrayList<>();
        for (Day d : days) {
            d.getWorkplaces().forEach(workplace -> {
                Map<String, Object> labelInfo = new HashMap<>();
                labelInfo.put("Date", d.getDate().toString());
                labelInfo.put("workplace", workplace.getName());
                labelInfo.put("color", workplace.getLabel());
                labels.add(labelInfo);
            });
        }

        return labels;
    }

    // 일별 근무저장기록
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getWorkList(String loginId, String date) {
        User user = userRepository.findByLoginId(loginId).orElse(null);
        if (user == null) return List.of();

        Optional<Day> day = user.getDays().stream()
            .filter(d -> d.getDate().equals(LocalDate.parse(date)))
            .findFirst();
        if(day.isEmpty()) return List.of();

        Day d = day.get();
        return d.getWorkplaces().stream().map(w -> {
            Map<String, Object> schedule = new HashMap<>();
            Duration duration = Duration.between(w.getStartTime(), w.getFinalTime());
            String hour = String.valueOf(duration.toHours());
            String minutes = String.valueOf(duration.toMinutes() % 60);

            schedule.put("workplace", w.getName());
            schedule.put("color", w.getLabel());
            schedule.put("workTime", w.getStartTime().toLocalTime() + " - " + w.getFinalTime().toLocalTime());
            schedule.put("hour", hour + "H " + minutes + "M");
            schedule.put("todaySalary", w.getTodayPay());
            return schedule;
        }).collect(Collectors.toList());
    }

    // 일급 계산
    @Transactional
    public void calculateTodayWage(Long day_id, Long workplace_id){
        Day day = dayRepository.findById(day_id).orElse(null);

        if (day != null) {
            day.getWorkplaces().stream()
                .filter(w -> w.getWorkplace_id().equals(workplace_id))
                .findFirst()
                .ifPresent(w -> {
                    Duration duration = Duration.between(w.getStartTime(), w.getFinalTime());

                    double night = calculateNightWorkHours(w.getStartTime(), w.getFinalTime(), w.getNightbreak());
                    double normal = (duration.toMinutes() - night * 60 - w.getBreaktime()) / 60.0;

                    double wage = w.getWage();

                    w.setTodayPay((wage * normal) + (wage * 1.5 * night));
                    workplaceRepository.save(w);
                });
            calculateDailyWage(day_id);
        }
    }

    private double calculateNightWorkHours(LocalDateTime startTime, LocalDateTime endTime, int nightBreak) {
        // 야간 근무 시작과 종료 시간 설정
        LocalTime nightStartTime = LocalTime.of(22, 0); // 야간 시작 시간
        LocalTime nightEndTime = LocalTime.of(6, 0);   // 야간 종료 시간 (다음 날 오전 6시)

        // 근무 당일 날짜와 다음 날짜
        LocalDate today = startTime.toLocalDate();
        LocalDate tomorrow = today.plusDays(1);

        // 야간 근무 시간 구간 설정
        LocalDateTime nightStartDateTime = LocalDateTime.of(today, nightStartTime);
        LocalDateTime nightEndDateTime = LocalDateTime.of(tomorrow, nightEndTime);

        // 야간 근무 시간 계산
        double nightWorkHours = 0;

        if (startTime.isBefore(nightStartDateTime) && endTime.isAfter(nightEndDateTime))
            nightWorkHours = Duration.between(nightStartDateTime, nightEndDateTime).toHours();

        else if (startTime.isBefore(nightStartDateTime) && endTime.isAfter(nightStartDateTime))
            nightWorkHours = Duration.between(nightStartDateTime, endTime).toHours();

        else if (startTime.isBefore(nightEndDateTime) && endTime.isAfter(nightEndDateTime))
            nightWorkHours = Duration.between(startTime, nightEndDateTime).toHours();

        else if (startTime.isAfter(nightStartDateTime) && endTime.isBefore(nightEndDateTime))
            nightWorkHours = Duration.between(startTime, endTime).toHours();


        nightWorkHours -= nightBreak / 60.0;

        return Math.max(nightWorkHours, 0);
    }

    // 일별 임금 계산 -> 월 임금까지 영향
    @Transactional
    public void calculateDailyWage(Long day_id) {
        Day day = dayRepository.findById(day_id).orElse(null);

        if (day != null) {
            double dailyWage = day.getWorkplaces().stream()
                .mapToDouble(Workplace::getTodayPay)
                .sum();

            day.setDailyWage(dailyWage);
            dayRepository.save(day);
        }
    }

    // 월별 임금 & 월 근무시간 패스
    @Transactional
    public Map<String, Object> getMonthlyInfo(String loginId, String startDate, String endDate) {
        User user = userRepository.findByLoginId(loginId).orElse(null);
        if (user == null) return Map.of();

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<Day> days = user.getDays().stream()
             .filter(day -> !day.getDate().isBefore(start) && !day.getDate().isAfter(end))
             .toList();

        long workHour = days.stream()
                .flatMap(day -> day.getWorkplaces().stream())
                .mapToLong(workplace -> {
                    Duration duration = Duration.between(workplace.getStartTime(), workplace.getFinalTime());
                    return duration.toMinutes();
                }).sum();
        long hours = workHour / 60;
        long minutes = workHour % 60;

        double total = days.stream()
             .mapToDouble(Day::getDailyWage)
             .sum();

        // user의 해당 달과 월급과 상이하면, 업데이트 & 없으면 생성
        Month month = start.getMonth();
        Optional<UserSalary> userSalary = userSalaryRepository.findByUserAndMonth(user, month);

        if (userSalary.isPresent()) {
            UserSalary check = userSalary.get();
            if (check.getSalary() != total) {
                check.setSalary(total);
                userSalaryRepository.save(check);
            }
        }
        else {
            UserSalary check = new UserSalary();
            check.setUser(user);
            check.setMonth(month);
            check.setSalary(total);
            userSalaryRepository.save(check);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("date", startDate + " - " + endDate);
        res.put("monthlyWage", total);
        res.put("totalHour", hours + "H " + minutes + "M");

        System.out.println("여기까지 오는건가?");
        return res;
    }

    // 환율 적용한 월급 패스
}
