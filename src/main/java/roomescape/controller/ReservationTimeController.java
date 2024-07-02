package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.ReservationTimeService;
import roomescape.dto.AvailableTimeResponse;
import roomescape.dto.ReservationTimeAddRequestDto;
import roomescape.entities.ReservationTime;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/times")
public class ReservationTimeController {
  private final ReservationTimeService reservationTimeService;

  @PostMapping
  public ResponseEntity<ReservationTime> saveTime(
    @RequestBody ReservationTimeAddRequestDto reservationTimeAddRequestDto) {
    ReservationTime reservationTime = reservationTimeService.saveTime(
      reservationTimeAddRequestDto);
    return ResponseEntity.ok().body(reservationTime);
  }

  @GetMapping
  public List<ReservationTime> findAllTimes() {
    return reservationTimeService.findAllTimes();
  }

  @DeleteMapping("/{id}")
  public void deleteTime(@PathVariable("id") Long id){
    reservationTimeService.cancelReservationTime(id);
  }

  @GetMapping("/available")
  public List<AvailableTimeResponse> findAvailableTimes(
    @RequestParam("date") String date, @RequestParam("themeId") Long themeId){
    return reservationTimeService.findAvailableTimes(date, themeId);
  }
}
