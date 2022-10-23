package com.nextstep.web.reservation.repository.entity;

import com.nextstep.web.schedule.repository.entity.ScheduleEntity;
import lombok.Getter;
import nextstep.domain.Identity;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.schedule.Schedule;

import java.time.LocalDateTime;

@Getter
public class ReservationEntity {
    private Long id;
    private ScheduleEntity scheduleEntity;
    private LocalDateTime reservationTime;
    private String name;

    public ReservationEntity(Long id, ScheduleEntity scheduleEntity, LocalDateTime reservationTime, String name) {
        this.id = id;
        this.scheduleEntity = scheduleEntity;
        this.reservationTime = reservationTime;
        this.name = name;
    }

    public Reservation fromThis() {
        return new Reservation(new Identity(id), scheduleEntity.fromThis(), reservationTime, name);
    }
}

