package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservationTheme.ReservationTheme;
import roomescape.reservationTheme.ReservationThemeResponseDto;
import roomescape.reservationTime.ReservationTime;
import roomescape.reservationTime.ReservationTimePolicy;
import roomescape.reservationTime.ReservationTimeResponseDto;

import java.util.List;
import java.util.Map;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationResponseDto> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(
                        reservation -> new ReservationResponseDto.Builder()
                                .id(reservation.getId())
                                .name(reservation.getName())
                                .date(reservation.getDate())
                                .reservationTimeResponseDto(new ReservationTimeResponseDto(
                                        reservation.getReservationTime().getId(),
                                        reservation.getReservationTime().getStartAt()))
                                .reservationThemeResponseDto(new ReservationThemeResponseDto(
                                        reservation.getReservationTheme().getId(),
                                        reservation.getReservationTheme().getName(),
                                        reservation.getReservationTheme().getDescription(),
                                        reservation.getReservationTheme().getThumbnail()
                                ))
                                .build()
                ).toList();
    }

    @Transactional
    public ReservationResponseDto save(ReservationRequestDto reservationRequestDto) {
        final Reservation reservation = new Reservation.Builder()
                .name(reservationRequestDto.getName())
                .date(reservationRequestDto.getDate())
                .reservationTime(
                        new ReservationTime(
                                reservationRequestDto.getReservationTimeRequestDto().getId(),
                                reservationRequestDto.getReservationTimeRequestDto().getStartAt()
                        ))
                .reservationTheme(
                        new ReservationTheme(
                                reservationRequestDto.getReservationThemeRequestDto().getId(),
                                reservationRequestDto.getReservationThemeRequestDto().getName(),
                                reservationRequestDto.getReservationThemeRequestDto().getDescription(),
                                reservationRequestDto.getReservationThemeRequestDto().getThumbnail()
                        )
                )
                .build();

        final Long savedId = reservationRepository.save(reservation);
        final Reservation savedReservation = reservationRepository.findById(savedId);

        return new ReservationResponseDto.Builder()
                .id(savedReservation.getId())
                .name(savedReservation.getName())
                .date(savedReservation.getDate())
                .reservationTimeResponseDto(
                        new ReservationTimeResponseDto(
                                savedReservation.getReservationTime().getId(),
                                savedReservation.getReservationTime().getStartAt()))
                .reservationThemeResponseDto(
                        new ReservationThemeResponseDto(
                                savedReservation.getReservationTheme().getId(),
                                savedReservation.getReservationTheme().getName(),
                                savedReservation.getReservationTheme().getDescription(),
                                savedReservation.getReservationTheme().getThumbnail()
                        )
                )
                .build();
    }


    @Transactional
    public void delete(Long id) {
        final boolean isExistedReservation = reservationRepository.existsById(id);
        if (!isExistedReservation) {
            throw new IllegalArgumentException("해당 예약이 존재하지 않습니다.");
        }
        reservationRepository.deleteById(id);
    }


    public ReservationResponseDto findById(Long id) {
        Reservation reservation = reservationRepository.findById(id);
        return new ReservationResponseDto.Builder()
                .id(reservation.getId())
                .name(reservation.getName())
                .date(reservation.getDate())
                .reservationTimeResponseDto(
                        new ReservationTimeResponseDto(
                                reservation.getReservationTime().getId(),
                                reservation.getReservationTime().getStartAt()))
                .build();
    }


    public List<ReservationTimeResponseDto> findAvaliableTimes(final String date, final Long themeId) {
        List<ReservationTime> availableReservationTimes = reservationRepository.getAvailableReservationTimes(date, themeId);
        return availableReservationTimes.stream()
                .map(
                        reservationTime -> new ReservationTimeResponseDto(
                                reservationTime.getId(),
                                reservationTime.getStartAt()
                        )
                ).toList();


    }
}
