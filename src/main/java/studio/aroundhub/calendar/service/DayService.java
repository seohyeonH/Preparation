package studio.aroundhub.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.aroundhub.calendar.repository.*;
import studio.aroundhub.calendar.repository.DayRepository;
import studio.aroundhub.calendar.repository.WorkplaceRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DayService {
    private final DayRepository dayRepository;
    private final WorkplaceRepository workplaceRepository;

    // 일별 근무저장기록
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getWorkList(String date) {
        Optional<Day> day = dayRepository.findByDate(LocalDate.parse(date));

        if(day.isEmpty()) return List.of();

        Day d = day.get();
        return d.getWorkplaces().stream().map(w -> {
            Map<String, Object> schedule = new HashMap<>();
            Duration duration = Duration.between(w.getStartTime(), w.getFinalTime());
            String hour = String.valueOf(duration.toHours());
            String minutes = String.valueOf(duration.toMinutes() % 60);

            schedule.put("id", w.getWorkplace_id());
            schedule.put("workplace", w.getName());
            schedule.put("color", w.getLabel());
            schedule.put("workTime", w.getStartTime() + " - " + w.getFinalTime());
            schedule.put("hour", hour + "H " + minutes + "M");
            schedule.put("todaySalary", w.getTodayPay());
            return schedule;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void calculateTodayWage(Long day_id, Long workplace_id){
        Day day = dayRepository.findById(day_id).orElse(null);

        if (day != null) {
            day.getWorkplaces().stream()
                    .filter(w -> w.getWorkplace_id().equals(workplace_id))
                    .findFirst()
                    .ifPresent(w -> {
                        Duration duration = Duration.between(w.getStartTime(), w.getFinalTime());

                        double total = duration.toHours() + ((duration.toMinutesPart() - w.getBreaktime()) % 60 / 60.0);
                        double night = calculateNightWorkHours(w.getStartTime(), w.getFinalTime(), w.getNightbreak());
                        double normal = total - night;

                        double wage = w.getWage();

                        w.setTodayPay((wage * normal) + (wage * 1.5 * night));
                        workplaceRepository.save(w);
                    });

            calculateDailyWage(day_id);
        }
    }

    private double calculateNightWorkHours(LocalDateTime startTime, LocalDateTime endTime, int nightBreak) {
        LocalTime Start = LocalTime.of(22, 0);
        LocalTime End = LocalTime.of(6, 0);

        LocalDateTime nightStartDateTime = LocalDateTime.of(startTime.toLocalDate(), Start);
        LocalDateTime nightEndDateTime = LocalDateTime.of(endTime.toLocalDate(), End);

        // 필요한 경우, 날짜 조정
        if (startTime.toLocalTime().isAfter(End) && startTime.toLocalTime().isBefore(Start))
            nightStartDateTime = nightStartDateTime.plusDays(1);
        if (endTime.toLocalTime().isAfter(End) && endTime.toLocalTime().isBefore(Start))
            nightEndDateTime = nightEndDateTime.plusDays(1);

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

        return nightWorkHours;
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

    // 월별 임금 패스
    @Transactional(readOnly = true)
    public Map<String, Object> getMonthlySalary(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<Day> days = dayRepository.findByDateBetween(start, end);

        double total = days.stream()
                .mapToDouble(Day::getDailyWage)
                .sum();

        Map<String, Object> res = new HashMap<>();
        res.put("date", startDate + " - " + endDate);
        res.put("monthlyWage", total);

        return res;
    }

}
