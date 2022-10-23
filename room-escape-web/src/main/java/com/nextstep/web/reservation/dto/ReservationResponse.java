package com.nextstep.web.reservation.dto;

import com.nextstep.web.reservation.repository.entity.ReservationEntity;
import com.nextstep.web.schedule.repository.entity.ScheduleEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.schedule.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ReservationResponse {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private String name;

    private ReservationResponse(Long id, LocalDate date, LocalTime time, String name) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.name = name;
    }

    public static List<ReservationResponse> toListFrom(List<Reservation> reservations) {

        return reservations.stream()
                .map(reservation ->
                        new ReservationResponse(reservation.getId().getNumber(),
                                reservation.getSchedule().getDate(),
                                reservation.getSchedule().getTime(),
                                reservation.getName()))
                .collect(Collectors.toList());
    }
}
