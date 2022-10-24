package nextstep.application.controller.admin;

import nextstep.application.service.schedule.ScheduleCommandService;
import nextstep.application.service.theme.ThemeCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ScheduleCommandService scheduleCommandService;
    private final ThemeCommandService themeCommandService;

    public AdminController(
        ScheduleCommandService scheduleCommandService,
        ThemeCommandService themeCommandService
    ) {
        this.scheduleCommandService = scheduleCommandService;
        this.themeCommandService = themeCommandService;
    }

    @DeleteMapping("/schedules")
    public ResponseEntity<Void> deleteSchedule(@RequestParam Long id) {
        scheduleCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/themes")
    public ResponseEntity<Void> deleteTheme(@RequestParam Long id) {
        themeCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
