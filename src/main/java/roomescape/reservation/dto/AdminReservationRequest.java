package roomescape.reservation.dto;

public class AdminReservationRequest extends ReservationRequest{

    private Long memberId;

    public AdminReservationRequest(Long memberId, String date, Long timeId, Long themeId) {
        super(date, timeId, themeId);
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
