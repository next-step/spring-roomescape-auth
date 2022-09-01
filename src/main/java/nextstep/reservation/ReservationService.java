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

    public Long create(ReservationRequest reservationRequest) {
        Reservation reservation = reservationDao.findByDateAndTime(reservationRequest.getDate(), reservationRequest.getTime());
        if (reservation != null) {
            throw new RuntimeException();
        }

        Reservation newReservation = new Reservation(
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
            throw new RuntimeException();
        }

        reservationDao.deleteByDateAndTime(date, time);
    }
}
