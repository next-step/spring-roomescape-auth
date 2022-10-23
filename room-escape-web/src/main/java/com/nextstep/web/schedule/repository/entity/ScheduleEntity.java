package com.nextstep.web.schedule.repository.entity;

import com.nextstep.web.theme.repository.entity.ThemeEntity;
import lombok.Getter;
import nextstep.domain.Identity;
import nextstep.domain.schedule.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ScheduleEntity {
    private Long id;
    private ThemeEntity themeEntity;
    private String date;
    private String time;

    public ScheduleEntity(Long id, ThemeEntity themeEntity, String date, String time) {
        this.id = id;
        this.themeEntity = themeEntity;
        this.date = date;
        this.time = time;
    }

    public Schedule fromThis() {
        return new Schedule(new Identity(id), themeEntity.fromThis(), LocalDate.parse(date), LocalTime.parse(time));
    }
}
