package studio.aroundhub.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.aroundhub.calendar.repository.Day;
import studio.aroundhub.calendar.repository.DayRepository;
import studio.aroundhub.calendar.repository.Workplace;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WorkplaceService {
    private final DayRepository dayRepository;
    private final DayService dayService;

    // Add Work
    @Transactional
    public void addWork(Map<String, Object> payload) {
        @SuppressWarnings("unchecked")
        Map<String, Object> workplace = (Map<String, Object>) payload.get("workplace");
        @SuppressWarnings("unchecked")
        List<String> dates = (List<String>) payload.get("dates");

        Workplace w = new Workplace();
        w.setName((String) workplace.get("workplace"));
        w.setLabel((String) workplace.get("color"));
        w.setType((String) workplace.get("type"));
        w.setStartTime(LocalDateTime.from(LocalTime.parse((String) workplace.get("startTime"))));
        w.setFinalTime(LocalDateTime.from(LocalTime.parse((String) workplace.get("endTime"))));
        w.setBreaktime((Integer) workplace.get("breakTime"));
        w.setNightbreak((Integer) workplace.get("nightBreak"));
        if(w.isCalculatemin()) w.setWage(9860);
        else w.setWage((Double) workplace.get("hourlyRate"));

        List<Day> selected = new ArrayList<>();

        for (String ds : dates) {
            LocalDate date = LocalDate.parse(ds);
            Day day = dayRepository.findByDate(date).orElseGet(() -> {
                Day newD = new Day();
                newD.setDate(date);
                return newD;
            });

            Workplace newW = new Workplace(
                    w.getWorkplace_id(),
                    w.getName(),
                    w.getLabel(),
                    w.getType(),
                    w.getBreaktime(),
                    w.getNightbreak(),
                    w.getStartTime(),
                    w.getFinalTime(),
                    w.getWage(),
                    day
            );
            day.getWorkplaces().add(newW);
            selected.add(day);
        }
        dayRepository.saveAll(selected);

        for (Day d : selected)
            dayService.calculateTodayWage(d.getId(), w.getWorkplace_id());
    }

    // Delete Work
    @Transactional
    public void deleteWork(Map<String, Object> payload) {
        Long workplace_id = ((Number) payload.get("workplaceId")).longValue();
        LocalDate date = LocalDate.parse((String) payload.get("date"));

        dayRepository.findByDate(date).ifPresent(day -> {
            day.getWorkplaces().removeIf(w-> w.getWorkplace_id().equals(workplace_id));
            dayRepository.save(day);
        });
    }

    // Modify Work
    @Transactional
    public void modifyWork(Map<String, Object> payload){
        @SuppressWarnings("unchecked")
        Map<String, Object> info = (Map<String, Object>) payload.get("info");
        String workplace = (String) payload.get("workplace");
        LocalDate date = LocalDate.parse((String) payload.get("date"));

        dayRepository.findByDate(date).ifPresent(day -> {
            List<Workplace> workplaces = day.getWorkplaces();

            workplaces.stream()
                .filter(w -> w.getName().equals(workplace))
                .findFirst()
                .ifPresent(w -> {
                    w.setLabel((String) info.get("color"));
                    w.setType((String) info.get("type"));
                    w.setBreaktime((Integer) info.get("breakTime"));
                    w.setNightbreak((Integer) info.get("nightBreak"));
                    w.setStartTime(LocalDateTime.of(date, LocalTime.parse((String) info.get("startTime"))));
                    w.setFinalTime(LocalDateTime.of(date, LocalTime.parse((String) info.get("finalTime"))));
                    if (w.isCalculatemin()) w.setWage(9860.0);
                    else w.setWage((Double) info.get("hourlyRate"));

                    dayRepository.save(day);
                });
        });
    }
}