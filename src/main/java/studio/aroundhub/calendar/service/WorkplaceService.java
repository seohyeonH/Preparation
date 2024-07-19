package studio.aroundhub.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.aroundhub.calendar.repository.Day;
import studio.aroundhub.calendar.repository.DayRepository;
import studio.aroundhub.calendar.repository.Workplace;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkplaceService {
    private final DayRepository dayRepository;
    private final CalendarService calendarService;

    // Add Work
    @Transactional
    public void addWork(List<Long> dayIds, Workplace workplace) {
        List<Day> selected = dayRepository.findAllById(dayIds);

        for (Day day : selected) {
            if (workplace.isCalculatemin()) workplace.setWage(9860.0);

            Workplace record = new Workplace(
                    workplace.getWorkplace_id(),
                    workplace.getName(),
                    workplace.getLabel(),
                    workplace.getType(),
                    workplace.getBreaktime(),
                    workplace.getNightbreak(),
                    workplace.getStartTime(),
                    workplace.getFinalTime(),
                    workplace.getWage(),
                    day
            );
            day.getWorkplaces().add(record);
        }
        dayRepository.saveAll(selected);

        for (Day day : selected)
            calendarService.calculateTodayWage(day.getId(), workplace.getWorkplace_id());
    }

    // Delete Work
    @Transactional
    public void deleteWork(Long dayId, Workplace workplace){
        dayRepository.findById(dayId).ifPresent(day -> {
            day.getWorkplaces().removeIf(w-> w.getWorkplace_id().equals(workplace.getWorkplace_id()));
            dayRepository.save(day);
        });
    }

    // Modify Work
    @Transactional
    public void modifyWork(Long dayId, Workplace workplace){
        dayRepository.findById(dayId).ifPresent(day -> {
            List<Workplace> workplaces = day.getWorkplaces();

            workplaces.stream()
                    .filter(w -> w.getWorkplace_id().equals(workplace.getWorkplace_id()))
                    .findFirst()
                    .ifPresent(w -> {
                        w.setName(workplace.getName());
                        w.setLabel(workplace.getLabel());
                        w.setType(workplace.getType());
                        w.setBreaktime(workplace.getBreaktime());
                        w.setNightbreak(workplace.getNightbreak());
                        w.setStartTime(workplace.getStartTime());
                        w.setFinalTime(workplace.getFinalTime());
                        if ((workplace.isCalculatemin())) w.setWage(9860);
                        else w.setWage(workplace.getWage());

                        dayRepository.save(day);
                    });
        });
    }
}