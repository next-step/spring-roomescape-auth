package roomescape.domain.time.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.time.api.dto.TimeRequest;
import roomescape.domain.time.api.dto.TimeResponse;
import roomescape.domain.time.domain.Time;
import roomescape.domain.time.service.TimeService;

import java.util.List;

@RestController
@RequestMapping("/times")
public class ApiTimeController {

    private final TimeService timeService;

    public ApiTimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @PostMapping
    public ResponseEntity<TimeResponse> save(@RequestBody TimeRequest timeRequest) {
        Time time = timeService.save(timeRequest);
        return ResponseEntity.ok().body(new TimeResponse(
                time.getId(),
                time.getDate(),
                time.getStartAt(),
                time.getTheme().getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        timeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
