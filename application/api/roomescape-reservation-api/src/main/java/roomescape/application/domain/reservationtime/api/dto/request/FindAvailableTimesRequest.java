package roomescape.application.domain.reservationtime.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import roomescape.application.domain.reservationtime.service.query.FindAvailableTimesQuery;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FindAvailableTimesRequest {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @NotBlank
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", message = "날짜는 yyyy-MM-dd 형식이어야 합니다.")
    private final String date;

    @Positive
    @NotNull(message = "이 값은 필수입니다.")
    private final Long themeId;

    public FindAvailableTimesRequest(String date, Long themeId) {
        this.date = date;
        this.themeId = themeId;
    }

    public FindAvailableTimesQuery toQuery() {
        return new FindAvailableTimesQuery(
                LocalDate.parse(this.date, DateTimeFormatter.ofPattern(DATE_FORMAT)),
                this.themeId
        );
    }

    public String getDate() {
        return date;
    }

    public Long getThemeId() {
        return themeId;
    }
}
