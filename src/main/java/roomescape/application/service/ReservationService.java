package roomescape.application.service;

import static roomescape.adapter.mapper.ReservationMapper.mapToDomain;
import static roomescape.adapter.mapper.ReservationMapper.mapToResponse;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.adapter.mapper.ReservationMapper;
import roomescape.application.dto.ReservationCommand;
import roomescape.application.dto.ReservationResponse;
import roomescape.application.port.in.ReservationUseCase;
import roomescape.application.port.out.ReservationPort;
import roomescape.application.port.out.ReservationTimePort;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.NotFoundReservationException;
import roomescape.exception.ReservationTimeConflictException;
import roomescape.validator.DateTimeValidator;

@Transactional
@Service
public class ReservationService implements ReservationUseCase {

  private final ReservationPort reservationPort;
  private final ReservationTimePort reservationTimePort;

  public ReservationService(ReservationPort reservationPort, ReservationTimePort reservationTimePort) {
    this.reservationPort = reservationPort;
    this.reservationTimePort = reservationTimePort;
  }

  @Transactional(readOnly = true)
  @Override
  public List<ReservationResponse> retrieveReservations() {
    return reservationPort.findReservations()
                          .stream()
                          .map(ReservationMapper::mapToResponse)
                          .toList();
  }

  @Override
  public ReservationResponse registerReservation(ReservationCommand reservationCommand) {
    DateTimeValidator.previousDateTimeCheck(reservationCommand.date(), reservationCommand.time());
    Reservation reservation = mapToDomain(reservationCommand);
    ReservationTime reservationTime = reservationTimePort.findReservationTimeByStartAt(reservation.getTime()
                                                                                                  .getStartAt())
                                                         .orElseThrow(NotFoundReservationException::new);

    if (reservationPort.findReservationByReservationTime(reservationTime)
                       .isPresent()) {
      throw new ReservationTimeConflictException();
    }

    return mapToResponse(reservationPort.saveReservation(reservation, reservationTime));
  }

  @Override
  public void deleteReservation(Long id) {
    if (reservationPort.countReservationById(id) == 0) {
      throw new NotFoundReservationException();
    }

    reservationPort.deleteReservation(id);
  }
}
