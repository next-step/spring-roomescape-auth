package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import roomescape.annotations.ValidationGroups.NotBlankGroup;
import roomescape.annotations.ValidationGroups.PatternGroup;

public class ReservationTimeRequest {

    @NotBlank(message = "시간은 필수 값입니다.", groups = NotBlankGroup.class)
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$"
            , message = "HH:mm 형식이 아닙니다."
            , groups = PatternGroup.class)
    private String startAt;

    public ReservationTimeRequest() {
    }

    public ReservationTimeRequest(String startAt) {
        this.startAt = startAt;
    }

    public String getStartAt() {
        return startAt;
    }
}
