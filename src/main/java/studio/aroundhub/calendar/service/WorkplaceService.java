package studio.aroundhub.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.aroundhub.calendar.repository.Day;
import studio.aroundhub.calendar.repository.DayRepository;
import studio.aroundhub.calendar.repository.Workplace;
import studio.aroundhub.calendar.repository.WorkplaceRepository;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkplaceService {
    private final WorkplaceRepository workplaceRepository;
    private final DayRepository dayRepository;

    // Add Work
    @Transactional
    public void addWorkplace(List<Long> dayIds, Workplace workplace) {
        List<Day> selected = dayRepository.findAllById(dayIds);

        for (Day day : selected) {
            boolean exists = day.getWorkplaces().stream()
                    .anyMatch(w -> w.getName().equals(workplace.getName()) && w.getType().equals(workplace.getType()));

            if (!exists) {
                Workplace recordNew = new Workplace(
                        null,
                        workplace.getName(),
                        workplace.getLabel(),
                        workplace.getType(),
                        workplace.getBreaktime(),
                        workplace.getNightbreak(),
                        workplace.getStartTime(),
                        workplace.getFinalTime(),
                        workplace.getWage(),
                        workplace.isSLpay(),
                        workplace.isCalculatemin(),
                        day
                );
                day.getWorkplaces().add(recordNew);
            }
        }
        dayRepository.saveAll(selected);
    }

    // Delete Work
    @Transactional
    public void deleteWorkplace(Long dayId, Workplace workplace){
        dayRepository.findById(dayId).ifPresent(day -> {
            day.getWorkplaces().removeIf(w-> w.getWorkplace_id().equals(workplace.getWorkplace_id()));
            dayRepository.save(day);
        });
    }

    // Modify Work
    @Transactional
    public void modifyWorkplace(Long dayId, Workplace workplace){
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
                        w.setWage(workplace.getWage());
                        w.setSLpay(workplace.isSLpay());
                        w.setCalculatemin(workplace.isCalculatemin());

                        dayRepository.save(day);
                    });
        });
    }

    // Calculate today_wage
    // 주휴수당, 야간근무, 쉬는시간 모두 고려해야 함.
    // 다시
    @Transactional
    public void calculateTodayWage(Long dayId, Workplace workplace){
        Day day = dayRepository.findById(dayId).orElse(null);

        if (day != null){
           double today = 0;
           for (Workplace w : day.getWorkplaces()){
               Duration duration = Duration.between(w.getStartTime(), w.getFinalTime());

               double hours;
               if(w.getBreaktime() > 0) hours = duration.toHours() + ((duration.toMinutes() - w.getBreaktime()) % 60 / 60.0);
               else hours = duration.toHours() + (duration.toMinutes() % 60 / 60.0);

               if(w.isCalculatemin()) w.setWage(9860); // 최저임금으로 계산
               if(w.isSLpay()) w.setWage(w.getWage() * 1.5); // 주휴수당 계산
               if(w.getNightbreak() > 0) w.setWage(w.getWage() * 1.2); // 야간근무 수당 계산

               w.setToday_pay(w.getWage() * hours);
           }
        }
    }
}