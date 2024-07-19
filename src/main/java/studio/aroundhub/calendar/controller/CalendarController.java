package studio.aroundhub.calendar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;
import studio.aroundhub.calendar.repository.*;
import studio.aroundhub.member.repository.User;
import studio.aroundhub.calendar.service.CalendarService;
import studio.aroundhub.calendar.service.DayService;
import studio.aroundhub.calendar.service.WorkplaceService;
import studio.aroundhub.member.repository.UserRepository;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/{user_id}")
public class CalendarController {
    private final WorkplaceService workplaceService;
    private final DayService dayService;
    private final CalendarService calendarService;
    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final DayRepository dayRepository;

    @GetMapping("/calendars")
    public ResponseEntity<List<Calendar>> getCalendars(@PathVariable("user_id") long user_id) {
        List<Calendar> calendars = calendarService.getCalendars(user_id);
        return ResponseEntity.ok(calendars);
    }

    @GetMapping("/{calender_id}/days")
    public ResponseEntity<List<Day>> getDays(@PathVariable("user_id") Long user_id, @PathVariable("calender_id") Long calendar_id){
        List<Day> days = dayService.getDays(calendar_id);

        if(days.isEmpty()) return ResponseEntity.ok(null);
        return ResponseEntity.ok(days);
    }

    // Add work
    @PostMapping("/{calender_id}/add-work")
    public ResponseEntity<?> addWork(@RequestParam List<Long> dayIds, @RequestBody Workplace workplace){
        workplaceService.addWork(dayIds, workplace);
        return ResponseEntity.ok().build();
    }

    // Delete work
    @DeleteMapping("/{calender_id}/delete-work")
    public ResponseEntity<?> deleteWork(@RequestBody Workplace workplace) {
        workplaceService.deleteWork(workplace.getDay().getId(), workplace);
        calendarService.calculateTodayWage(workplace.getDay().getId(), workplace.getWorkplace_id());
        return ResponseEntity.ok().build();
    }

    // Modify work
    @PutMapping("/{calender_id}/add-work")
    public ResponseEntity<?> modifyWork(@RequestBody Workplace workplace) {
        workplaceService.modifyWork(workplace.getDay().getId(), workplace);
        calendarService.calculateTodayWage(workplace.getDay().getId(), workplace.getWorkplace_id());
        return ResponseEntity.ok().build();
    }

    // 일일 근무저장기록
    @GetMapping("/show-work")
    public ResponseEntity<?> showWork(@RequestParam Long day_id) {
        List<Workplace> workplaces = dayService.getWorkList(day_id);

        if(workplaces == null)
            return ResponseEntity.notFound().build();

        // 라벨 색상, 근무지 이름, 근무 시간대, 근무시간, 임금
        List<Map<String, Object>> workplaceDetail = workplaces.stream().map(w -> {
            Map<String, Object> data = new HashMap<>();
            data.put("label", w.getLabel());
            data.put("workplaceName", w.getName());
            data.put("workTime", w.getStartTime().toString() + " - " + w.getFinalTime().toString());

            Duration d = Duration.between(w.getStartTime(), w.getFinalTime());
            double hours = d.toHours() + ((d.toMinutesPart()) % 60 / 60.0);
            data.put("workHours", hours);
            data.put("wage", w.getTodayPay());

            return data;
        }).toList();

        return ResponseEntity.ok(workplaceDetail);
    }
}