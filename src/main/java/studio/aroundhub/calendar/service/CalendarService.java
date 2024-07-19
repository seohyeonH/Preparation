package studio.aroundhub.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.aroundhub.calendar.repository.*;
import studio.aroundhub.member.repository.User;
import studio.aroundhub.member.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    private final DayRepository dayRepository;
    private final WorkplaceRepository workplaceRepository;

    // user의 달력 확인
    @Transactional(readOnly = true)
    public List<Calendar> getCalendars(Long user_id) {
        User user = userRepository.findById(user_id).orElse(null);

        if (user != null) return user.getCalendars();
        return new ArrayList<>();
    }

    // 직장별 임금 계산 -> 일별 임금까지 영향
    @Transactional
    public void calculateTodayWage(Long day_id, Long workplace_id){
        Day day = dayRepository.findById(day_id).orElse(null);

        if (day != null) {
            for (Workplace w : day.getWorkplaces()) {
                Duration duration = Duration.between(w.getStartTime(), w.getFinalTime());

                double total = duration.toHours() + ((duration.toMinutesPart() - w.getBreaktime()) % 60 / 60.0);
                double night = calculateNightWorkHours(w.getStartTime(), w.getFinalTime(), w.getNightbreak());
                double normal = total - night;

                double wage = w.getWage();

                w.setTodayPay((wage * normal) + (wage * 1.5 * night));
                workplaceRepository.save(w);
            }
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

        // Subtract night break time from night work hours
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

            calculateMonthlyWage(day.getCalendar().getId());
        }
    }

    // 월별 임금 계산
    @Transactional
    public void calculateMonthlyWage(Long calendar_id) {
        Calendar calendar = calendarRepository.findById(calendar_id).orElse(null);

        if(calendar != null){
            List<Day> days = calendar.getDays();
            double totalWage = days.stream()
                    .mapToDouble(Day::getDailyWage)
                    .sum();

            calendar.setMonthlyWage(totalWage);
            calendarRepository.save(calendar);
        }
    }
}
