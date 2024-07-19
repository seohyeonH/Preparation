package studio.aroundhub.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.aroundhub.calendar.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

@Service
@RequiredArgsConstructor
public class DayService {
    private final DayRepository dayRepository;
    private final CalendarRepository calendarRepository;

    // 데이터가 기록되어 있는 일자 확인
    @Transactional(readOnly = true)
    public List<Day> getDays(Long Calendar_id){
        Calendar calendar = calendarRepository.findById(Calendar_id).orElse(null);

        if(calendar != null) return calendar.getDays();
        return new ArrayList<>();
    }

    // 일별 근무저장기록
    @Transactional(readOnly = true)
    public List<Workplace> getWorkList(Long day_id) {
        Day day = dayRepository.findById(day_id).orElse(null);

        return (day != null) ? day.getWorkplaces() : null;
    }
}
