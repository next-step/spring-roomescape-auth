package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReservationAdminRequest {
    @NotBlank(message = "날짜가 입력되지 않았습니다.")
    private String date;

    @NotNull(message = "테마가 선택되지 않았습니다.")
    private Long themeId;

    @NotNull(message = "예약 시간이 선택되지 않았습니다.")
    private Long timeId;

    @NotNull(message = "회원이 선택되지 않았습니다.")
    private Long memberId;

    public ReservationAdminRequest() {

    }

    public ReservationAdminRequest(String date, Long themeId, Long timeId, Long memberId) {
        this.date = date;
        this.themeId = themeId;
        this.timeId = timeId;
        this.memberId = memberId;
    }

    public String getDate() {
        return date;
    }

    public Long getThemeId() {
        return themeId;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
