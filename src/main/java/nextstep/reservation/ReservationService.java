package nextstep.reservation;

import java.util.List;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

  public final ReservationDao reservationDao;
  public final ThemeDao themeDao;
  public final ScheduleDao scheduleDao;

  public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao) {
    this.reservationDao = reservationDao;
    this.themeDao = themeDao;
    this.scheduleDao = scheduleDao;
  }

  public Long create(ReservationRequest reservationRequest) {
    Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
    if (schedule == null) {
      throw new NullPointerException();
    }

    List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
    if (!reservation.isEmpty()) {
      throw new DuplicateEntityException();
    }

    Reservation newReservation = new Reservation(
        schedule,
        reservationRequest.getName()
    );

    return reservationDao.save(newReservation);
  }

  public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
    Theme theme = themeDao.findById(themeId);
    if (theme == null) {
      throw new NullPointerException();
    }

    return reservationDao.findAllByThemeIdAndDate(themeId, date);
  }

  public void deleteById(Long id) {
    Reservation reservation = reservationDao.findById(id);
    if (reservation == null) {
      throw new NullPointerException();
    }

    reservationDao.deleteById(id);
  }

  public Reservation findById(Long id) {
    return reservationDao.findById(id);
  }
}
