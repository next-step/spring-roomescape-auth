package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import roomescape.annotations.ValidationGroups.NotBlankGroup;
import roomescape.annotations.ValidationGroups.PatternGroup;

public class ReservationRequest {

    @NotBlank(message = "날짜는 필수 값입니다.", groups = NotBlankGroup.class)
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$"
            , message = "yyyy-MM-dd 형식이 아닙니다."
            , groups = PatternGroup.class)
    private String date;

    @NotNull(message = "예약하려면 시간을 선택해야합니다. 확인 후 다시 시도해주세요.")
    private Long timeId;

    @NotNull(message = "예약하려면 테마를 선택해야합니다. 확인 후 다시 시도해주세요.")
    private Long themeId;

    public ReservationRequest() {
    }

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
