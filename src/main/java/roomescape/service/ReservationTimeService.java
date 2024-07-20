package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.time.ReservationTimeRequest;
import roomescape.dto.time.ReservationTimeResponse;
import roomescape.dto.time.available.ReservationTimeAvailableResponse;
import roomescape.dto.time.create.ReservationTimeCreateResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.custom.DuplicatedReservationTimeException;
import roomescape.exception.custom.ThemeNotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationTimeService(ReservationRepository reservationRepository,
                                  ReservationTimeRepository reservationTimeRepository,
                                  ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;

    }

    public List<ReservationTimeResponse> findTimes() {
        return reservationTimeRepository.findTimes()
                .stream()
                .map(ReservationTimeResponse::new)
                .collect(Collectors.toList());
    }

    public ReservationTimeCreateResponse createTime(ReservationTimeRequest request) {
        checkDuplicatedReservationTime(request.getStartAt());
        ReservationTime changedReservationTime = ReservationTime.from(request);
        ReservationTime reservationTime = reservationTimeRepository.createTime(changedReservationTime);
        return ReservationTimeCreateResponse.toDto(reservationTime);
    }

    public void deleteTime(Long id) {
        reservationTimeRepository.deleteTime(id);
    }

    public void checkDuplicatedReservationTime(String startAt) {
        int count = reservationTimeRepository.countReservationTimeByStartAt(startAt);
        if (count > 0) {
            throw new DuplicatedReservationTimeException("이미 존재하는 예약 시간입니다. 다른 시간대를 입력해주세요.");
        }
    }

    public List<ReservationTimeAvailableResponse> findAvailableReservationTime(String date, Long themeId) {
        Theme theme = themeRepository.findThemeById(themeId).orElseThrow(() ->
                new ThemeNotFoundException(ErrorCode.THEME_NOT_FOUND,"해당 테마가 존재하지 않습니다."));

        // 모든 예약 시간 조회
        List<ReservationTime> times = reservationTimeRepository.findTimes();

        // 이미 예약된 시간 조회
        List<ReservationTime> reservedTimes = reservationRepository.findReservedTimesByDateAndTheme(date, theme);

        // 예약 가능한 시간
        List<ReservationTime> collect = times.stream()
                .filter(time -> reservedTimes.stream()
                        .noneMatch(reservedTime -> reservedTime.getStartAt().equals(time.getStartAt())))
                .collect(Collectors.toList());

        return collect.stream()
                .map(ReservationTimeAvailableResponse::fromReservationTime)
                .toList();
    }
}
