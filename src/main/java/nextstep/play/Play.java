package nextstep.play;

import java.util.Objects;

public class Play {

    private Long id;
    private Long reservationId;
    private Long memberId;
    private boolean hidden;

    protected Play() {
    }

    public Play(Long reservationId, Long memberId) {
        this(null, reservationId, memberId, false);
    }

    public Play(
        Long id,
        Long reservationId,
        Long memberId,
        boolean hidden
    ) {
        this.id = id;
        this.reservationId = reservationId;
        this.memberId = memberId;
        this.hidden = hidden;
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

    public boolean isHidden() {
        return hidden;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Play play = (Play) o;
        return Objects.equals(id, play.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
