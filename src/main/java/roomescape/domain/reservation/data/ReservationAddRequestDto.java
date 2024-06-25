package roomescape.domain.reservation.data;

import lombok.Getter;

@Getter
public class ReservationAddRequestDto {
    private String name;

    private String date;

    private Long timeId;

    private Long themeId;

    public ReservationAddRequestDto() {
    }

    public ReservationAddRequestDto(String name,
                                    String date,
                                    Long timeId,
                                    Long themeId) {
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }
}
