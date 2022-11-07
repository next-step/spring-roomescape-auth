package nextstep.schedule;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

  private final ScheduleService scheduleService;

  public ScheduleController(ScheduleService scheduleService) {
    this.scheduleService = scheduleService;
  }

  @PostMapping("/admin")
  public ResponseEntity createSchedule(@RequestBody ScheduleRequest scheduleRequest) {
    Long id = scheduleService.create(scheduleRequest);
    return ResponseEntity.created(URI.create("/schedules/" + id)).build();
  }

  @GetMapping
  public ResponseEntity<List<Schedule>> showReservations(@RequestParam Long themeId, @RequestParam String date) {
    return ResponseEntity.ok().body(scheduleService.findByThemeIdAndDate(themeId, date));
  }

  @DeleteMapping("/{id}/admin")
  public ResponseEntity deleteReservation(@PathVariable Long id) {
    scheduleService.deleteById(id);

    return ResponseEntity.noContent().build();
  }
}
