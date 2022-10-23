package com.nextstep.web.reservation.repository;

import com.nextstep.web.reservation.repository.entity.ReservationEntity;
import com.nextstep.web.schedule.repository.ScheduleDao;
import com.nextstep.web.schedule.repository.entity.ScheduleEntity;
import com.nextstep.web.theme.repository.entity.ThemeEntity;
import nextstep.domain.Identity;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.reservation.usecase.ReservationRepository;
import nextstep.domain.schedule.Schedule;
import nextstep.domain.theme.Theme;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private final ReservationDao reservationDao;
    private final ScheduleDao scheduleDao;

    public JdbcReservationRepository(ReservationDao reservationDao, ScheduleDao scheduleDao) {
        this.reservationDao = reservationDao;
        this.scheduleDao = scheduleDao;
    }

    @Override
    public Long save(Reservation reservation) {
        Schedule schedule =  reservation.getSchedule();
        Theme theme = schedule.getTheme();
        ThemeEntity themeEntity = new ThemeEntity(theme.getId().getNumber(), theme.getName(), theme.getDesc(), theme.getPrice());
        ScheduleEntity scheduleEntity =
                new ScheduleEntity(schedule.getId().getNumber(),
                        themeEntity, schedule.getDate().toString(), schedule.getTime().toString());
        return reservationDao.save(new ReservationEntity(null, scheduleEntity, reservation.getReservationTime(),
                reservation.getName()));
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservationDao.findById(id).map(
                entity -> new Reservation(new Identity(entity.getId()),
                        entity.getScheduleEntity().fromThis(), entity.getReservationTime(), entity.getName())
        );
    }

    @Override
    public Optional<Reservation> findByScheduleId(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Reservation> findByMemberName(String name) {
        return reservationDao.findByMemberName(name).stream()
                .map(entity -> new Reservation(new Identity(entity.getId()),
                        entity.getScheduleEntity().fromThis(), entity.getReservationTime(), entity.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findAllBy(String date) {
        List<Long> scheduleIds = scheduleDao.findAllBy(date).stream()
                .map(scheduleEntity -> scheduleEntity.getId())
                .collect(Collectors.toList());

        return reservationDao.findAllBy(scheduleIds).stream()
                .map(entity -> new Reservation(new Identity(entity.getId()),
                        entity.getScheduleEntity().fromThis(), entity.getReservationTime(), entity.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        reservationDao.delete(id);
    }

}
