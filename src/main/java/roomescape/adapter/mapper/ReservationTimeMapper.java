package roomescape.adapter.mapper;


import roomescape.adapter.out.ReservationTimeEntity;
import roomescape.application.dto.ReservationTimeCommand;
import roomescape.application.dto.ReservationTimeResponse;
import roomescape.domain.ReservationTime;

public class ReservationTimeMapper {

  private ReservationTimeMapper() {
    throw new UnsupportedOperationException("생성 불가");
  }

  public static ReservationTime mapToDomain(ReservationTimeCommand reservationTimeCommand) {
    return ReservationTime.of(null, reservationTimeCommand.startAt());
  }

  public static ReservationTime mapToDomain(ReservationTimeEntity reservationTimeEntity) {
    return ReservationTime.of(reservationTimeEntity.getId(), reservationTimeEntity.getStartAt());
  }

  public static ReservationTimeResponse mapToResponse(ReservationTime reservationTime) {
    return ReservationTimeResponse.of(reservationTime.getId(), reservationTime.getStartAt());
  }

  public static ReservationTimeEntity mapToEntity(ReservationTime reservationTime) {
    return ReservationTimeEntity.of(reservationTime.getId(), reservationTime.getStartAt());
  }
}
