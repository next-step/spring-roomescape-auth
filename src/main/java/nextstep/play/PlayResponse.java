package nextstep.play;

import static java.util.stream.Collectors.toList;

import java.util.List;

public class PlayResponse {

    private final Long id;
    private final Long reservationId;
    private final Long memberId;

    public PlayResponse(
        Long id,
        Long reservationId,
        Long memberId
    ) {
        this.id = id;
        this.reservationId = reservationId;
        this.memberId = memberId;
    }

    public static List<PlayResponse> of(List<Play> plays) {
        return plays.stream()
            .map(PlayResponse::from)
            .collect(toList());
    }

    public static PlayResponse from(Play play) {
        return new PlayResponse(
            play.getId(),
            play.getReservationId(),
            play.getMemberId()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
