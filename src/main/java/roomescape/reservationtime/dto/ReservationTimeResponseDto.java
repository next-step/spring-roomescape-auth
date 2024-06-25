package roomescape.reservationtime.dto;

public class ReservationTimeResponseDto {

    private Long id;
    private String startAt;

    public ReservationTimeResponseDto(Long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }

    @Override
    public String toString() {
        return "{ " +
                "id=" + id +
                ", startAt='" + startAt + '\'' +
                '}';
    }
}
