package studio.aroundhub.calendar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import studio.aroundhub.calendar.service.DayService;
import studio.aroundhub.calendar.service.WorkplaceService;
import studio.aroundhub.pado.service.ExchangeService;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class CalendarController {
    private final WorkplaceService workplaceService;
    private final DayService dayService;
    private final ExchangeService exchangeService;

    // 월 라벨 데이터 패스
    @GetMapping("/calendar")
    public ResponseEntity<List<Map<String, Object>>> getLabels(@RequestParam("userId") String userId, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        List<Map<String, Object>> res = dayService.getLabels(userId, startDate, endDate);
        return ResponseEntity.ok(res);
    }

    // 월별 임금  & 월 근무시간
    @GetMapping("/calculate")
    public ResponseEntity<Map<String, Object>> getMonthlyInfo(@RequestParam("userId") String userId, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        Map<String, Object> monthlyData = dayService.getMonthlyInfo(userId, startDate, endDate);

        if(monthlyData.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(monthlyData);
    }

    // 근무 저장기록
    @GetMapping("/schedule")
    public ResponseEntity<List<Map<String, Object>>> showSchedule(@RequestParam("userId") String userId, @RequestParam("selectedDate") String selectedDate) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(selectedDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        List<Map<String, Object>> schedules = dayService.getWorkList(userId, String.valueOf(offsetDateTime.toLocalDate()));

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

    // Modify work (정보 확인)
    @GetMapping("/modify")
    public ResponseEntity<Map<String, Object>> getWorkplace(@RequestBody Map<String, Object> payload) {
        Map<String, Object> data = workplaceService.modify(payload);

        if(data.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(data);
    }
}