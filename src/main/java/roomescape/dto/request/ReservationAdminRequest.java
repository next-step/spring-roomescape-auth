package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ReservationAdminRequest {
    @NotBlank(message = "날짜가 입력되지 않았습니다.")
    private String date;
    @NotBlank(message = "테마가 선택되지 않았습니다.")
    private Long themeId;
    @NotBlank(message = "예약 시간이 선택되지 않았습니다.")
    private Long timeId;
    @NotBlank(message = "회원이 선택되지 않았습니다.")
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
