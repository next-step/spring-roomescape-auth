package roomescape.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReservationAddRequestDto {
    private String name;

    private String date;

    private Long timeId;

    private Long themeId;
}
