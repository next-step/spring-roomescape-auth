package roomescape.reservationTime.service;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.error.exception.ReservationTimeReferenceException;
import roomescape.error.exception.ThemeNotExistsException;
import roomescape.reservation.service.ReservationRepository;

import java.util.List;
import java.util.stream.Collectors;
import roomescape.reservationTime.ReservationTime;
import roomescape.reservationTime.dto.ReservationTimeRequest;
import roomescape.reservationTime.dto.ReservationTimeResponse;
import roomescape.theme.Theme;
import roomescape.theme.service.ThemeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                    ReservationRepository reservationRepository,
                                    ThemeRepository themeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationTimeResponse saveReservationTime(ReservationTimeRequest request) {
        ReservationTime reservationTime = new ReservationTime(request.getStartAt());
        return new ReservationTimeResponse(reservationTimeRepository.save(reservationTime));
    }

    public List<ReservationTimeResponse> findReservationTimes() {
        return reservationTimeRepository.find().stream()
            .map(ReservationTimeResponse::new)
            .collect(Collectors.toList());
    }

    public void deleteReservationTime(Long id) {
        if (reservationRepository.countByTime(id) > 0) {
            throw new ReservationTimeReferenceException();
        }

        reservationTimeRepository.delete(id);
    }

    public List<ReservationTimeResponse> findAvailableReservationTimes(String date, Long themeId) {
        Theme theme = Optional.ofNullable(themeRepository.findById(themeId))
            .orElseThrow(ThemeNotExistsException::new);

        return reservationTimeRepository.findAvailableTimesByDateAndTheme(LocalDate.parse(date),
                theme.getId()).stream()
            .map(ReservationTimeResponse::new)
            .collect(Collectors.toList());
    }
}
