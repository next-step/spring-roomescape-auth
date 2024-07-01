package roomescape.reservationtime.application;

import org.springframework.stereotype.Service;
import roomescape.reservationtime.ui.dto.ReservationTimeRequest;
import roomescape.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.reservationtime.domain.entity.ReservationTime;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.reservationtime.domain.ReservationTimeRepository;

import java.util.List;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationTimeValidator reservationTimeValidator;

    public ReservationTimeService(
            ReservationTimeRepository reservationTimeRepository,
            ReservationTimeValidator reservationTimeValidator) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationTimeValidator = reservationTimeValidator;
    }

    public List<ReservationTimeResponse> findAll() {
        List<ReservationTime> times = reservationTimeRepository.findAll();
        return ReservationTimeResponse.fromReservationTimes(times);
    }

    public ReservationTimeResponse findOne(Long id) {
        ReservationTime time = reservationTimeRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("id에 일치하는 예약 시간이 없습니다."));
        return ReservationTimeResponse.from(time);
    }

    public List<ReservationTimeResponse> findMatchWith(String date, Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findMatchWith(date, themeId);
        return ReservationTimeResponse.fromReservationTimes(reservationTimes);
    }

    public ReservationTimeResponse add(ReservationTimeRequest request) {
        reservationTimeValidator.validateRequest(request);
        ReservationTime reservationTime = ReservationTime.from(request);
        Long reservationTimeId = reservationTimeRepository.save(reservationTime);
        reservationTime.setId(reservationTimeId);
        return ReservationTimeResponse.from(reservationTime);
    }

    public void delete(Long id) {
        checkReservationMatchWith(id);
        long deleteCount = reservationTimeRepository.deleteById(id);

        if (deleteCount == 0) {
            throw NotFoundException.of("id와 일치하는 예약 시간이 없습니다.");
        }
    }

    private void checkReservationMatchWith(Long id) {
        if (reservationTimeRepository.countReservationMatchWith(id) > 0) {
            throw BadRequestException.of("해당 시간에 대한 예약이 존재합니다.");
        }
    }
}
