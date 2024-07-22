package studio.aroundhub.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.aroundhub.calendar.repository.Day;
import studio.aroundhub.calendar.repository.DayRepository;
import studio.aroundhub.calendar.repository.Workplace;
import studio.aroundhub.calendar.repository.WorkplaceRepository;
import studio.aroundhub.member.repository.User;
import studio.aroundhub.member.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkplaceService {
    private final DayRepository dayRepository;
    private final UserRepository userRepository;
    private final WorkplaceRepository workplaceRepository;
    private final DayService dayService;

    // Add Work
    @Transactional
    public void addWork(Map<String, Object> payload) {
        @SuppressWarnings("unchecked")
        Map<String, Object> workplace = (Map<String, Object>) payload.get("workplace");
        @SuppressWarnings("unchecked")
        List<String> dates = (List<String>) payload.get("workDates");
        String userId = (String) payload.get("userId");

        User user = userRepository.findByLoginId(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (user == null) return;

        List<Day> selected = new ArrayList<>();

        for (String ds : dates) {
            LocalDate date = LocalDate.parse(ds);
            Day day = dayRepository.findByDate(date).orElseGet(() -> {
                Day newD = new Day();
                newD.setDate(date);
                newD.setUser(user);
                newD.setWorkplaces(new ArrayList<>());
                return newD;
            });

            Workplace w = new Workplace();
            w.setName((String) workplace.get("workplace"));
            w.setLabel((String) workplace.get("color"));
            w.setType((String) workplace.get("type"));
            w.setStartTime(LocalDateTime.of(date, LocalTime.parse((String) workplace.get("startTime"))));
            w.setFinalTime(LocalDateTime.of(date, LocalTime.parse((String) workplace.get("endTime"))));
            w.setBreaktime((Integer) workplace.get("breakTime"));
            w.setNightbreak((Integer) workplace.get("nightBreak"));
            w.setCalculatemin((Boolean) workplace.get("IsMin"));
            w.setWage((Boolean) workplace.get("IsMin") ? 9860.0 : ((Number) workplace.get("hourlyRate")).doubleValue());
            w.setDay(day);

            day.getWorkplaces().add(w);
            if (!user.getDays().contains(day)) user.getDays().add(day);

            selected.add(day);
        }
        dayRepository.saveAll(selected);

        for (Day d : selected)
            dayService.calculateTodayWage(d.getId(), d.getWorkplaces().get(0).getWorkplace_id());
    }

    // Delete Work
    @Transactional
    public void deleteWork(Map<String, Object> payload) {
        String userId = (String) payload.get("userId");
        String workplace = ((String) payload.get("workplace"));
        LocalDate date = LocalDate.parse((String) payload.get("date"));

        User user = userRepository.findByLoginId(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (user == null) return;

        dayRepository.findByUserAndDate(user, date).ifPresent(day -> {
            List<Workplace> list = day.getWorkplaces().stream()
                    .filter(w -> w.getName().equals(workplace))
                    .toList();

            if(!list.isEmpty())
                for (Workplace w : list) {
                    workplaceRepository.delete(w);
                    day.getWorkplaces().remove(w);
                }

            dayService.calculateDailyWage(day.getId());
            dayRepository.save(day);
        });
    }

    @Transactional(readOnly = true)
    public Map<String, Object> modify(Map<String, Object> payload) {
        String userId = (String) payload.get("userId");
        String workplace = (String) payload.get("workplace");
        LocalDate date = LocalDate.parse((String) payload.get("date"));

        User user = userRepository.findByLoginId(userId).orElse(null);
        if (user == null) return Map.of();

        return dayRepository.findByUserAndDate(user, date)
            .map(day -> day.getWorkplaces().stream()
            .filter(w -> w.getName().equals(workplace))
            .findFirst()
            .map(res -> {
                Map<String, Object> result = new HashMap<>();
                result.put("workplace", res.getName());
                result.put("workDate", String.valueOf(date));
                result.put("color", res.getLabel());
                result.put("startTime", res.getStartTime().toLocalTime());
                result.put("finalTime", res.getFinalTime().toLocalTime());
                result.put("todayPay", res.getTodayPay());
                result.put("breakTime", res.getBreaktime());
                result.put("nightbreak", res.getNightbreak());
                result.put("hourlyRate", res.getWage());
                result.put("IsMin", res.isCalculatemin());

                return result;
                }).orElse(Map.of()))
        .orElse(Map.of());
    }

    // Modify Work
    @Transactional
    public void modifyWork(Map<String, Object> payload){
        @SuppressWarnings("unchecked")
        Map<String, Object> workplace = (Map<String, Object>) payload.get("workplace");
        LocalDate date = LocalDate.parse((String) payload.get("date"));
        String userId = (String) payload.get("userId");

        User user = userRepository.findByLoginId(userId).orElse(null);

        dayRepository.findByUserAndDate(user, date).ifPresent(day -> {
            List<Workplace> workplaces = day.getWorkplaces();

            workplaces.stream()
                .filter(w -> w.getName().equals((String) workplace.get("workplace")))
                .findFirst()
                .ifPresent(w -> {
                    w.setName((String) workplace.get("workplace"));
                    w.setLabel((String) workplace.get("color"));
                    w.setType((String) workplace.get("type"));
                    w.setStartTime(LocalDateTime.of(date, LocalTime.parse((String) workplace.get("startTime"))));
                    w.setFinalTime(LocalDateTime.of(date, LocalTime.parse((String) workplace.get("endTime"))));
                    w.setBreaktime((Integer) workplace.get("breakTime"));
                    w.setNightbreak((Integer) workplace.get("nightBreak"));
                    w.setCalculatemin((Boolean) workplace.get("IsMin"));
                    w.setWage((Boolean) workplace.get("IsMin") ? 9860.0 : ((Number) workplace.get("hourlyRate")).doubleValue());
                    w.setDay(day);

                    dayRepository.save(day);
                    dayService.calculateTodayWage(w.getDay().getId(), w.getWorkplace_id());
                });
        });
    }
}