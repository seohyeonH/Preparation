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

    // 월 라벨 데이터 패스
    @GetMapping("/calendar")
    public ResponseEntity<List<Map<String, Object>>> getLabels(@RequestParam("userId") String userId, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        List<Map<String, Object>> labels = dayService.getLabels(userId, startDate, endDate);

        if (labels.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(labels);
    }

    // 월별 임금  & 월 근무시간
    @GetMapping("/calculate")
    public ResponseEntity<Map<String, Object>> getMonthlyInfo(@RequestBody Map<String, Object> payload) {
        Map<String, Object> monthlyData = dayService.getMonthlyInfo(
                (String)payload.get("userId"),
                (String) payload.get("startDate"),
                (String) payload.get("endDate"));

        if(monthlyData.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(monthlyData);
    }

    // 근무 저장기록
    @GetMapping("/schedule")
    public ResponseEntity<List<Map<String, Object>>> showSchedule(@RequestBody Map<String, Object> payload) {
        List<Map<String, Object>> schedules = dayService.getWorkList(
                (String) payload.get("userId"),
                (String) payload.get("selectedDate"));

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

    // Modify work (정보 확인)
    @GetMapping("/modify")
    public ResponseEntity<Map<String, Object>> getWorkplace(@RequestBody Map<String, Object> payload) {
        Map<String, Object> data = workplaceService.modify(payload);

        if(data.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(data);
    }

    // Modify work 2 (정보 수정) -> 확인
    @PutMapping("/schedule")
    public ResponseEntity<?> modifyWork(@RequestBody Map<String, Object> payload) {
        workplaceService.modifyWork(payload);
        return ResponseEntity.ok().build();
    }
}