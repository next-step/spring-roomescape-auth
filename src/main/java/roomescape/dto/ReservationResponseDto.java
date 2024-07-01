package roomescape.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import roomescape.entities.Reservation;
import roomescape.entities.ReservationTime;
import roomescape.entities.Theme;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class ReservationResponseDto {
    private Long id;

    private String name;

    private Theme theme;

    private String date;

    private ReservationTime time;

    public static ReservationResponseDto fromEntity(Reservation reservation) {
        return new ReservationResponseDto(
          reservation.getId(),
          reservation.getName(),
          reservation.getTheme(),
          reservation.getDate(),
          reservation.getReservationTime());
    }

    public static List<ReservationResponseDto> toEntities(List<Reservation> reservations){
        return reservations.stream()
          .map(ReservationResponseDto::fromEntity)
          .collect(Collectors.toList());
    }
}
