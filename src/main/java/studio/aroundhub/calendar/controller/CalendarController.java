package studio.aroundhub.calendar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import studio.aroundhub.calendar.repository.Calendar;
import studio.aroundhub.calendar.repository.CalendarRepository;
import studio.aroundhub.calendar.service.CalendarService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ba;da")
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping("/{user_id}/calendars")
    public ResponseEntity<List<Calendar>> getUserCalendars(@PathVariable("user_id") long user_id) {
        List<Calendar> calendars = calendarService.getCalendars(user_id);
        return ResponseEntity.ok(calendars);
    }
}