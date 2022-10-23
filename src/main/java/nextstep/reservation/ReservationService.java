package nextstep.reservation;

import nextstep.member.MemberDao;
import nextstep.reservation.presentation.dto.ReservationRequest;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
    }

    public Long create(ReservationRequest reservationRequest, String uuid) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        Long memberId = memberDao.findByUuid(uuid).orElseThrow().getId();
        Reservation newReservation = new Reservation(
                schedule,
                memberId
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

    public void deleteById(Long id, String uuid) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        Long memberId = memberDao.findByUuid(uuid).orElseThrow().getId();
        if(!reservation.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("해당 회원이 취소할 수 없는 예약입니다.");
        }

        reservationDao.deleteById(id);
    }
}
