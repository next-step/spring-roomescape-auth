package roomescape.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import roomescape.entities.ReservationTime;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class AvailableTimeResponse {
  private Long timeId;

  private String startAt;

  public static List<AvailableTimeResponse> listOf(List<ReservationTime> reservationTimes) {
    return reservationTimes.stream()
      .map(reservationTime -> new AvailableTimeResponse(reservationTime.getId(), reservationTime.getStartAt()))
      .collect(Collectors.toList());
  }
}
