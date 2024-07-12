package studio.aroundhub.calendar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CalendarController {

    @RequestMapping("/api/calendar")
    public String Calendar() {
        return "index";
    }
}