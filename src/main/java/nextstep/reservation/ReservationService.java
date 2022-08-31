package nextstep.reservation;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {
    public final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public void create(ReservationRequest reservationRequest) {
        Reservation reservation = new Reservation(
                LocalDate.parse(reservationRequest.getDate()),
                LocalTime.parse(reservationRequest.getTime() + ":00"),
                reservationRequest.getName()
        );

        reservationDao.save(reservation);
    }

    public List<Reservation> readByDate(String date) {
        return reservationDao.findByDate(date);
    }

    public void delete(String date, String time) {
        reservationDao.deleteByDateAndTime(date, time);
    }
}
