package roomescape.time.application;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.repository.ThemeRepository;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.repository.ReservationTimeRepository;
import roomescape.time.dto.ReservationTimeCreateRequest;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.exception.CannotDeleteReserveTimeException;
import roomescape.time.exception.ReservationTimeAlreadyExistsException;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
            ReservationRepository reservationRepository, ThemeRepository themeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationTimeResponse createReservationTime(ReservationTimeCreateRequest request) {
        if (reservationTimeRepository.existsByStartAt(request.startAt())) {
            throw new ReservationTimeAlreadyExistsException();
        }
        ReservationTime savedResponseTime = reservationTimeRepository.save(request.toEntity());
        return ReservationTimeResponse.from(savedResponseTime);
    }

    public List<ReservationTimeResponse> getReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<ReservationTimeResponse> getAvailableReservationTimes(LocalDate date, Long themeId) {
        Theme findTheme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeNotFoundException());
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        return reservationTimes.stream()
                .filter(time ->
                        !reservationRepository.existsByDateAndReservationTimeAndTheme(date.toString(), time, findTheme))
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public void deleteReservationTime(Long reservationTimeId) {
        if (reservationRepository.existsByReservationTimeId(reservationTimeId)) {
            throw new CannotDeleteReserveTimeException();
        }
        reservationTimeRepository.deleteById(reservationTimeId);
    }
}
