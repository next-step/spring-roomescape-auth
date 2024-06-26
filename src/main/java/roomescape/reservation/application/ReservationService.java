package roomescape.reservation.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.infra.ReservationRepository;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservation.dto.ReservationResponseDto;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtheme.domain.ReservationTheme;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.ReservationTimeResponseDto;

import java.util.List;

import static roomescape.reservation.dto.ReservationResponseDto.reservationResponseDtoFromReservation;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationResponseDto> findAll() {
        final List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(reservation -> reservationResponseDtoFromReservation(reservation)
                ).toList();
    }

    @Transactional
    public ReservationResponseDto save(final ReservationRequestDto reservationRequestDto) {
        final Reservation reservation = new Reservation.Builder()
                .name(reservationRequestDto.getName())
                .date(reservationRequestDto.getDate())
                .reservationTime(new ReservationTime(
                    reservationRequestDto.getReservationTimeRequestDto().getTimeId()))
                .reservationTheme(new ReservationTheme(
                    reservationRequestDto.getReservationThemeRequestDto().getThemeId()))
                .build();
        log.info("reservation : {}", reservation);
        final Long savedId = reservationRepository.save(reservation);
        final Reservation savedReservation = reservationRepository.findById(savedId);

        return reservationResponseDtoFromReservation(savedReservation);
    }


    @Transactional
    public void delete(final Long id) {
        final boolean isExistedReservation = reservationRepository.existsById(id);
        if (!isExistedReservation) {
            throw new IllegalArgumentException("해당 예약이 존재하지 않습니다.");
        }
        reservationRepository.deleteById(id);
    }


    public ReservationResponseDto findById(final Long id) {
        final Reservation reservation = reservationRepository.findById(id);
        return reservationResponseDtoFromReservation(reservation);
    }


    public List<ReservationTimeResponseDto> findAvaliableTimes(final String date, final Long themeId) {
        final List<ReservationTime> availableReservationTimes = reservationRepository.getAvailableReservationTimes(date, themeId);
        return availableReservationTimes.stream()
                .map(reservationTime -> new ReservationTimeResponseDto(
                    reservationTime.getTimeId(),
                    reservationTime.getStartAt()
                )).toList();
    }
}
