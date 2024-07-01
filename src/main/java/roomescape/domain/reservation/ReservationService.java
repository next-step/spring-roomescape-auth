package roomescape.domain.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationResponseDto;
import roomescape.entities.Reservation;
import roomescape.errors.ErrorCode;
import roomescape.exceptions.RoomEscapeException;
import roomescape.repositories.ReservationRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReservationService {
  private final ReservationRepository reservationRepository;

  public List<ReservationResponseDto> findAllReservations() {
    List<Reservation> reservations = reservationRepository.findAll();

    return ReservationResponseDto.toEntities(reservations);
  }

  public Reservation saveReservation(Reservation reservation) {
    Optional<Reservation> existingReservation = reservationRepository.findByDateAndTime(
      reservation.getDate(),
      reservation.getReservationTime().getStartAt());

    if (existingReservation.isPresent()) {
      throw new RoomEscapeException(ErrorCode.INVALID_INPUT_VALUE, "이미 예약된 시간입니다.");
    }

    return reservationRepository.save(reservation);
  }

  public void cancelReservation(Long id) {
    reservationRepository.deleteById(id);
  }
}
