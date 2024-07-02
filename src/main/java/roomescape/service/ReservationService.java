package roomescape.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationAddRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.entities.Reservation;
import roomescape.entities.ReservationTime;
import roomescape.entities.Theme;
import roomescape.exceptions.ErrorCode;
import roomescape.exceptions.RoomEscapeException;
import roomescape.repositories.ReservationRepository;

import java.util.List;

@Builder
@RequiredArgsConstructor
@Service
public class ReservationService {
  private final ReservationRepository reservationRepository;
  private final ReservationTimeService reservationTimeService;
  private final ThemeService themeService;

  public List<ReservationResponseDto> findAllReservations() {
    List<Reservation> reservations = reservationRepository.findAll();

    return ReservationResponseDto.toEntities(reservations);
  }

  public Reservation saveReservation(ReservationAddRequestDto reservationAddRequestDto) {

    ReservationTime reservationTime = reservationTimeService.findById(
      reservationAddRequestDto.getTimeId());

    reservationRepository.findByDateAndTime(
      reservationAddRequestDto.getDate(),
      reservationTime.getStartAt()).ifPresent(reservation -> {
      throw new RoomEscapeException(ErrorCode.INVALID_INPUT_VALUE, "이미 예약된 시간입니다.");
    });

    Theme theme = themeService.findById(reservationAddRequestDto.getThemeId());

    return reservationRepository.save(Reservation.builder()
      .name(reservationAddRequestDto.getName())
      .date(reservationAddRequestDto.getDate())
      .reservationTime(reservationTime)
      .theme(theme)
      .build());
  }

  public void cancelReservation(Long id) {
    reservationRepository.deleteById(id);
  }
}
