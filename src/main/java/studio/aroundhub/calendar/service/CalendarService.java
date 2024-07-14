package studio.aroundhub.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.aroundhub.calendar.repository.Calendar;
import studio.aroundhub.calendar.repository.CalendarRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;

    @Transactional(readOnly = true)
    public List<Calendar> getCalendars(Long user_id) {
        return calendarRepository.findAllById(user_id);
        // 더 채워야함
    }

    // 일마다 저장해놓은 라벨 데이터 갖고오기

    // 일마다 계산해둔 유저의 해당 일자 임금 갖고오기 -> 한달 임금 계산 시 필요.




}
