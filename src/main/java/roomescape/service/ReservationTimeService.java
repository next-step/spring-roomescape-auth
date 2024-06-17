package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exception.custom.DuplicateTimeException;
import roomescape.exception.custom.ReservationTimeConflictException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public ReservationTimeResponse createReservationTime(ReservationTimeRequest reservationTimeRequest) {
        Long count = reservationTimeDao.findByStartAt(reservationTimeRequest.getStartAt());
        if (count > 0) {
            throw new DuplicateTimeException();
        }

        ReservationTime reservationTime = reservationTimeDao.save(convertToEntity(reservationTimeRequest));
        return this.convertToResponse(reservationTime);
    }

    public List<ReservationTimeResponse> findAllReservationTimes() {
        return reservationTimeDao.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public void deleteReservationTime(Long id) {
        long count = reservationDao.countByTimeId(id);
        if (count > 0) {
            throw new ReservationTimeConflictException();
        }
        reservationTimeDao.delete(id);
    }

    public List<ReservationTimeResponse> findAllByAvailableTime(String date, Long themeId) {
        return this.convertToList(reservationTimeDao.findAllByAvailableTime(date, themeId));
    }

    private ReservationTimeResponse convertToResponse(ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt());
    }

    private ReservationTime convertToEntity(ReservationTimeRequest reservationTimeRequest) {
        return new ReservationTime(reservationTimeRequest.getStartAt());
    }

    private List<ReservationTimeResponse> convertToList(List<ReservationTime> reservationTimes) {
        return reservationTimes.stream().map(this::convertToResponse).toList();
    }
}
