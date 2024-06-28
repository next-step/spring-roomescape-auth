package roomescape.reservation.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReservationRequest {
    @JsonProperty(value = "name")
    private String memberName;
    @NotBlank
    private final String date;
    @NotNull
    private final Long timeId;
    @NotNull
    private final Long themeId;

    private ReservationRequest(String name, String date, Long timeId, Long themeId) {
        this.memberName = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public static ReservationRequest create(String name, String date, Long timeId, Long themeId) {
        return new ReservationRequest(name, date, timeId, themeId);
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

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
