package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.custom.ExistingReservationException;
import roomescape.exception.custom.InvalidReservationThemeException;
import roomescape.exception.custom.InvalidReservationTimeException;
import roomescape.exception.custom.PastDateReservationException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationThemeDao;
import roomescape.repository.ReservationTimeDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ReservationThemeDao reservationThemeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao,
                              ReservationThemeDao reservationThemeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.reservationThemeDao = reservationThemeDao;
    }

    public ReservationResponse createReservation(ReservationRequest reservationRequest) {
        validateReservationCreation(reservationRequest);

        Reservation reservation = reservationDao.save(this.convertToEntity(reservationRequest));
        return this.convertToResponse(reservation);
    }

    public List<ReservationResponse> findAllReservations() {
        return reservationDao.findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    public void deleteReservation(Long id) {
        reservationDao.delete(id);
    }

    private void validateReservationCreation(ReservationRequest reservationRequest) {
        findReservationThemeById(reservationRequest.getThemeId());
        ReservationTime reservationTime = findReservationTimeById(reservationRequest.getTimeId());

        long count = reservationDao.countByDateAndTimeIdAndThemeId(
                reservationRequest.getDate()
                , reservationRequest.getTimeId()
                , reservationRequest.getThemeId());
        if (count > 0) {
            throw new ExistingReservationException();
        }

        if (isDateExpired(reservationRequest.getDate(), reservationTime.getStartAt())) {
            throw new PastDateReservationException();
        }
    }

    private ReservationResponse convertToResponse(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getReservationDate(),
                reservation.getTime().getStartAt(), reservation.getTheme().getName());
    }

    private Reservation convertToEntity(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = findReservationTimeById(reservationRequest.getTimeId());
        ReservationTheme reservationTheme = findReservationThemeById(reservationRequest.getThemeId());

        return new Reservation(reservationRequest.getName(), reservationRequest.getDate(), reservationTime,
                reservationTheme);
    }

    private ReservationTime findReservationTimeById(String timeId) {
        return reservationTimeDao
                .findById(Long.parseLong(timeId))
                .orElseThrow(InvalidReservationTimeException::new);
    }

    private ReservationTheme findReservationThemeById(String themeId) {
        return reservationThemeDao
                .findById(Long.parseLong(themeId))
                .orElseThrow(InvalidReservationThemeException::new);
    }

    private boolean isDateExpired(String date, String startAt) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(startAt));

        return reservationDateTime.isBefore(now);
    }
}
