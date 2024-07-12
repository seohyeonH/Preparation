package studio.aroundhub.calendar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studio.aroundhub.calendar.repository.Calendar;
import studio.aroundhub.calendar.repository.CalendarRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;

    public List<Calendar> getCalendars(Long user_id) {
        return calendarRepository.findByUserId(user_id);
        // 더 채워야함
    }
}
