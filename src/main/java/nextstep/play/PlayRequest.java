package nextstep.play;

public class PlayRequest {

    private Long reservationId;

    protected PlayRequest() {
    }

    public PlayRequest(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getReservationId() {
        return reservationId;
    }
}
