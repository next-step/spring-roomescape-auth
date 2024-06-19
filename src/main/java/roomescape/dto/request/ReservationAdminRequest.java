package roomescape.dto.request;

public class ReservationAdminRequest {

    private String date;
    private Long themeId;
    private Long timeId;
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
