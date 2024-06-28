package roomescape.reservation.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReservationRequest {
    @NotBlank
    private final String memberName;
    @NotBlank
    private final String date;
    @NotNull
    private final Long timeId;
    @NotNull
    private final Long themeId;

    private ReservationRequest(String memberName, String date, Long timeId, Long themeId) {
        this.memberName = memberName;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public static ReservationRequest create(String memberName, String date, Long timeId, Long themeId) {
        return new ReservationRequest(memberName, date, timeId, themeId);
    }

    public static ReservationRequest fromCookieRequest(String memberName, CookieReservationRequest request) {
        return new ReservationRequest(
                memberName,
                request.date(),
                request.timeId(),
                request.themeId());
    }

    public String getMemberName() {
        return memberName;
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
