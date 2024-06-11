package roomescape.application.dto;

public record ReservationTimeResponse(Long id, String startAt) {

  public static ReservationTimeResponse of(Long id, String startAt) {
    return new ReservationTimeResponse(id, startAt);
  }
}
