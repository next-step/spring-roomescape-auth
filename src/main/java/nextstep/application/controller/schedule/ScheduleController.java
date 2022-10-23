package nextstep.application.controller;

import java.net.URI;
import java.util.List;
import nextstep.application.dto.schedule.ScheduleRequest;
import nextstep.application.dto.schedule.ScheduleResponse;
import nextstep.application.service.schedule.ScheduleCommandService;
import nextstep.application.service.schedule.ScheduleQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleCommandService scheduleCommandService;
    private final ScheduleQueryService scheduleQueryService;

    public ScheduleController(
        ScheduleCommandService scheduleCommandService,
        ScheduleQueryService scheduleQueryService
    ) {
        this.scheduleCommandService = scheduleCommandService;
        this.scheduleQueryService = scheduleQueryService;
    }

    @PostMapping
    public ResponseEntity<Void> make(@RequestBody ScheduleRequest request) {
        Long scheduleId = scheduleCommandService.make(request);
        return ResponseEntity.created(URI.create("/schedules/" + scheduleId)).build();
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> checkAll(
        @RequestParam Long themeId,
        @RequestParam String date
    ) {
        List<ScheduleResponse> responses = scheduleQueryService.checkAll(themeId, date);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping
    public ResponseEntity<Void> cancel(@RequestParam Long id) {
        scheduleCommandService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
