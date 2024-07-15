package studio.aroundhub.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.aroundhub.calendar.repository.Day;
import studio.aroundhub.calendar.repository.DayRepository;
import studio.aroundhub.calendar.repository.Workplace;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

@Service
@RequiredArgsConstructor
public class DayService {
    private final DayRepository dayRepository;

    // 일별 근무저장기록
    @Transactional(readOnly = true)
    public List<Workplace> getWorkplaces(Long dayId) {
        Day day = dayRepository.findById(dayId).orElse(null);

        if (day != null) return day.getWorkplaces();
        else return new ArrayList<>();
    }

    // 일별 임금 구하기
    @Transactional
    public void calculateDailyWage(Long dayId){
        Day day = dayRepository.findById(dayId).orElse(null);

        if(day != null){
            List<Workplace> workList = day.getWorkplaces();
            for (Workplace w : workList)
                synchronized (this) { // 동기화 처리
                    day.monthly_wage += w.today_pay;
                }

            dayRepository.save(day);
        }
    }

    // 직장별 라벨 색상 가져오기
    @Transactional(readOnly = true)
    public List<Color> getLables(Long dayId){
        Day day = dayRepository.findById(dayId).orElse(null);
        List<Color> labels = new ArrayList<>();

        if(day != null){
            List<Workplace> workList = day.getWorkplaces();
            for (Workplace w : workList)
                labels.add(w.getLabel());
        }

        return labels;
    }
}
