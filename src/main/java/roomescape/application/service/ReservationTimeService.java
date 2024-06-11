package roomescape.application.service;

import static roomescape.adapter.mapper.ReservationTimeMapper.mapToDomain;
import static roomescape.adapter.mapper.ReservationTimeMapper.mapToResponse;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.adapter.mapper.ReservationTimeMapper;
import roomescape.application.dto.ReservationTimeCommand;
import roomescape.application.dto.ReservationTimeResponse;
import roomescape.application.port.in.ReservationTimeUseCase;
import roomescape.application.port.out.ReservationPort;
import roomescape.application.port.out.ReservationTimePort;
import roomescape.domain.ReservationTime;
import roomescape.exception.InvalidSaveDuplicationReservationTime;
import roomescape.exception.NotFoundReservationTimeException;
import roomescape.exception.ReservationTimeConflictException;

@Transactional
@Service
public class ReservationTimeService implements ReservationTimeUseCase {

  private final ReservationTimePort reservationTimePort;
  private final ReservationPort reservationPort;

  public ReservationTimeService(ReservationTimePort reservationTimePort, ReservationPort reservationPort) {
    this.reservationTimePort = reservationTimePort;
    this.reservationPort = reservationPort;
  }

  @Override
  public ReservationTimeResponse registerReservationTime(ReservationTimeCommand reservationTimeCommand) {
    ReservationTime reservationTime = mapToDomain(reservationTimeCommand);

    if (reservationTimePort.findReservationTimeByStartAt(reservationTime.getStartAt())
                           .isPresent()) {
      throw new InvalidSaveDuplicationReservationTime();
    }

    return mapToResponse(reservationTimePort.saveReservationTime(reservationTime));
  }

  @Transactional(readOnly = true)
  @Override
  public List<ReservationTimeResponse> retrieveReservationTimes() {
    return reservationTimePort.findReservationTimes()
                              .stream()
                              .map(ReservationTimeMapper::mapToResponse)
                              .toList();
  }

  @Override
  public void deleteReservationTime(Long id) {
    ReservationTime reservationTime = reservationTimePort.findReservationTimeById(id)
                                                         .orElseThrow(
                                                           NotFoundReservationTimeException::new);

    if (reservationPort.findReservationByReservationTime(reservationTime)
                       .isPresent()) {
      throw new ReservationTimeConflictException();
    }

    reservationTimePort.deleteReservationTime(id);
  }
}
