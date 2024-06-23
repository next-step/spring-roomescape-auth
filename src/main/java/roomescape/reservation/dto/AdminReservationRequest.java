package roomescape.reservation.dto;

import jakarta.validation.constraints.NotNull;
import roomescape.error.RoomescapeErrorMessage;

public class AdminReservationRequest extends ReservationRequest{

    @NotNull(message = "회원 아이디" + RoomescapeErrorMessage.BLANK_INPUT_VALUE_EXCEPTION)
    private Long memberId;

    public AdminReservationRequest(Long memberId, String date, Long timeId, Long themeId) {
        super(date, timeId, themeId);
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
