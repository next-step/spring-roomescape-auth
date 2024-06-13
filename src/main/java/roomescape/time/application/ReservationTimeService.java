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
            throw new ReservationTimeAlreadyExistsException("이미 존재하는 시간입니다.");
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
                .orElseThrow(() -> new ThemeNotFoundException("해당 테마가 존재하지 않습니다."));
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        return reservationTimes.stream()
                .filter(time ->
                        !reservationRepository.existsByDateAndReservationTimeAndTheme(date.toString(), time, findTheme))
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public void deleteReservationTime(Long reservationTimeId) {
        if (reservationRepository.existsByReservationTimeId(reservationTimeId)) {
            throw new CannotDeleteReserveTimeException("예약된 시간은 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteById(reservationTimeId);
    }
}
