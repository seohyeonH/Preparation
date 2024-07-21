package studio.aroundhub.calendar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import studio.aroundhub.calendar.service.DayService;
import studio.aroundhub.calendar.service.WorkplaceService;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class CalendarController {
    private final WorkplaceService workplaceService;
    private final DayService dayService;

    // 일일 근무저장기록
    @GetMapping("/schedule")
    public ResponseEntity<List<Map<String, Object>>> showSchedule(@RequestParam String date) {
        List<Map<String, Object>> schedules = dayService.getWorkList(date);

        if(schedules.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(schedules);
    }

    // Add work
    @PostMapping("/schedule")
    public ResponseEntity<?> addWork(@RequestBody Map<String, Object> payload){
        workplaceService.addWork(payload);
        return ResponseEntity.ok().build();
    }

    // Delete work
    @DeleteMapping("/schedule")
    public ResponseEntity<?> deleteWork(@RequestBody Map<String, Object> payload) {
        workplaceService.deleteWork(payload);
        return ResponseEntity.ok().build();
    }

    // Modify work
    @PutMapping("/schedule")
    public ResponseEntity<?> modifyWork(@RequestBody Map<String, Object> payload) {
        workplaceService.modifyWork(payload);
        return ResponseEntity.ok().build();
    }

    // 월별 임금
    @GetMapping("/calculate")
    public ResponseEntity<Map<String, Object>> getMonthlySalary(@RequestParam String startDate, @RequestParam String endDate) {
        Map<String, Object> monthlyPay = dayService.getMonthlySalary(startDate, endDate);

        if(monthlyPay.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(monthlyPay);
    };
}