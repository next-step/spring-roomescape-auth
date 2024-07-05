package roomescape.application.domain.reservation.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import roomescape.application.domain.reservation.service.command.CreateReservationCommand;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreateReservationRequest {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @NotBlank
    private final String name;

    @NotBlank
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", message = "날짜는 yyyy-MM-dd 형식이어야 합니다.")
    private final String date;

    @Positive
    @NotNull(message = "이 값은 필수입니다.")
    private final Long timeId;

    @Positive
    @NotNull(message = "이 값은 필수입니다.")
    private final Long themeId;


    public CreateReservationRequest(String name, String date, Long timeId, Long themeId) {
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public CreateReservationCommand toCreateReservationCommand() {
        return new CreateReservationCommand(this.name, LocalDate.parse(this.date, DateTimeFormatter.ofPattern(DATE_FORMAT)), this.timeId, this.themeId);
    }

    public String getName() {
        return name;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getThemeId() {
        return themeId;
    }

    public String getDate() {
        return date;
    }
}
