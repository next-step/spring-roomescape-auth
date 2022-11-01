package nextstep.reservation;

import java.util.Objects;
import nextstep.auth.AuthenticationException;
import nextstep.auth.LoginInfo;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    public final MemberDao memberDao;
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;

    public ReservationService(
        MemberDao memberDao,
        ReservationDao reservationDao,
        ThemeDao themeDao,
        ScheduleDao scheduleDao
    ) {
        this.memberDao = memberDao;
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
    }

    public Long create(LoginInfo loginInfo, ReservationRequest reservationRequest) {
        if(loginInfo == null) {
            throw new AuthenticationException("비로그인 사용자는 예약이 불가능하다.");
        }

        Member member = memberDao.findByUsername(loginInfo.getUsername());
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new ReservationException("존재하지 않는 스케줄에 예약할 수 없습니다.");
        }

        if (!reservationDao.findByScheduleId(schedule.getId()).isEmpty()) {
            throw new ReservationException("이미 예약된 스케줄입니다.");
        }

        Reservation newReservation = new Reservation(schedule, member.getId());
        return reservationDao.save(newReservation);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new ReservationException("존재하지 않는 테마에 예약할 수 없습니다.");
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(LoginInfo loginInfo, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new ReservationException("존재하지 않는 예약입니다.");
        }

        Member member = memberDao.findByUsername(loginInfo.getUsername());
        if(!Objects.equals(reservation.getMemberId(), member.getId())) {
            throw new AuthenticationException("자신의 예약이 아닌 경우 예약 취소가 불가능하다.");
        }

        reservationDao.deleteById(id);
    }
}
