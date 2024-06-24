package roomescape.reservation.dto;

import roomescape.reservation.domain.Reservation;
import roomescape.reservationTheme.dto.ReservationThemeResponseDto;
import roomescape.reservationTime.dto.ReservationTimeResponseDto;

public class ReservationResponseDto {

    private Long id;
    private String name;
    private String date;
    private ReservationTimeResponseDto reservationTimeResponseDto;
    private ReservationThemeResponseDto reservationThemeResponseDto;

    public ReservationResponseDto(Long id, String name, String date, ReservationTimeResponseDto reservationTimeResponseDto, ReservationThemeResponseDto reservationThemeResponseDto) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTimeResponseDto = reservationTimeResponseDto;
        this.reservationThemeResponseDto = reservationThemeResponseDto;

    }

    public static class Builder {

        private Long id;
        private String name;
        private String date;
        private ReservationTimeResponseDto reservationTimeResponseDto;
        private ReservationThemeResponseDto reservationThemeResponseDto;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder reservationTimeResponseDto(ReservationTimeResponseDto reservationTimeResponseDto) {
            this.reservationTimeResponseDto = reservationTimeResponseDto;
            return this;
        }

        public Builder reservationThemeResponseDto(ReservationThemeResponseDto reservationThemeResponseDto) {
            this.reservationThemeResponseDto = reservationThemeResponseDto;
            return this;
        }

        public ReservationResponseDto build() {
            return new ReservationResponseDto(id, name, date, reservationTimeResponseDto, reservationThemeResponseDto);
        }
    }

    public static ReservationResponseDto reservationResponseDtoFromReservation(Reservation reservation){
        return new ReservationResponseDto.Builder()
                .id(reservation.getId())
                .name(reservation.getName())
                .date(reservation.getDate())
                .reservationTimeResponseDto(new ReservationTimeResponseDto(
                        reservation.getReservationTime().getId(),
                        reservation.getReservationTime().getStartAt()))
                .reservationThemeResponseDto(new ReservationThemeResponseDto(
                        reservation.getReservationTheme().getId(),
                        reservation.getReservationTheme().getName(),
                        reservation.getReservationTheme().getDescription(),
                        reservation.getReservationTheme().getThumbnail()
                )).build();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public ReservationTimeResponseDto getReservationTimeResponseDto() {
        return reservationTimeResponseDto;
    }

    public ReservationThemeResponseDto getReservationThemeResponseDto() {
        return reservationThemeResponseDto;
    }

    @Override
    public String toString() {
        return "{ " +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", reservationTimeResponseDto=" + reservationTimeResponseDto +
                ", reservationThemeResponseDto=" + reservationThemeResponseDto +
                '}';
    }
}
