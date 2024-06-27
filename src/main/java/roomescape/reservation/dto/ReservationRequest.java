package roomescape.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.error.RoomescapeErrorMessage;

public class ReservationRequest {

    @NotBlank(message = "날짜" + RoomescapeErrorMessage.BLANK_INPUT_VALUE_EXCEPTION)
    private String date;

    @NotNull(message = "시간" + RoomescapeErrorMessage.BLANK_INPUT_VALUE_EXCEPTION)
    private Long timeId;

    @NotNull(message = "테마" + RoomescapeErrorMessage.BLANK_INPUT_VALUE_EXCEPTION)
    private Long themeId;

    public ReservationRequest(String date, Long timeId, Long themeId) {
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public String getDate() {
        return date;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getThemeId() {
        return themeId;
    }
}
