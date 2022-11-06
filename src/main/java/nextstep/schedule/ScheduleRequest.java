package nextstep.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import nextstep.theme.Theme;

@Builder
@Jacksonized
public record ScheduleRequest(Long themeId, String date, String time) {

  public Schedule toEntity(Theme theme) {
    return new Schedule(
        theme,
        LocalDate.parse(this.date),
        LocalTime.parse(this.time)
    );
  }
}
