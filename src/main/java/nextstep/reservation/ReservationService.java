package nextstep.reservation;

import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
    }

    public Long create(ReservationRequest reservationRequest) {
        Reservation reservation = reservationDao.findByDateAndTime(reservationRequest.getDate(), reservationRequest.getTime());
        if (reservation != null) {
            throw new DuplicateEntityException();
        }

        Theme theme = themeDao.findById(reservationRequest.getThemeId());

        Reservation newReservation = new Reservation(
                theme,
                LocalDate.parse(reservationRequest.getDate()),
                LocalTime.parse(reservationRequest.getTime() + ":00"),
                reservationRequest.getName()
        );

        return reservationDao.save(newReservation);
    }

    public List<Reservation> readByDate(String date) {
        return reservationDao.findByDate(date);
    }

    public void delete(String date, String time) {
        Reservation reservation = reservationDao.findByDateAndTime(date, time);
        if (reservation == null) {
            throw new NullPointerException();
        }

        reservationDao.deleteByDateAndTime(date, time);
    }
}
