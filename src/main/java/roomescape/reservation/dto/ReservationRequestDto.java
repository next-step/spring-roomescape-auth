package roomescape.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import roomescape.reservationTheme.dto.ReservationThemeRequestDto;
import roomescape.reservationTime.dto.ReservationTimeRequestDto;

public class ReservationRequestDto {

    @NotBlank(message = "예약자 명 입력해주세요")
    private String name;
    @NotBlank(message = "예약일자를 입력해주세요")
    private String date;
    private ReservationTimeRequestDto reservationTimeRequestDto;
    private ReservationThemeRequestDto reservationThemeRequestDto;

    public ReservationRequestDto(String name, String date, ReservationTimeRequestDto reservationTimeRequestDto, ReservationThemeRequestDto reservationThemeRequestDto) {
        this.name = name;
        this.date = date;
        this.reservationTimeRequestDto = reservationTimeRequestDto;
        this.reservationThemeRequestDto = reservationThemeRequestDto;
    }

    public static class Builder {

        private String name;
        private String date;
        private ReservationTimeRequestDto reservationTimeRequestDto;
        private ReservationThemeRequestDto reservationThemeRequestDto;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder reservationTimeRequestDto(ReservationTimeRequestDto reservationTimeRequestDto) {
            this.reservationTimeRequestDto = reservationTimeRequestDto;
            return this;
        }

        public Builder reservationThemeRequestDto(ReservationThemeRequestDto reservationThemeRequestDto) {
            this.reservationThemeRequestDto = reservationThemeRequestDto;
            return this;
        }

        public ReservationRequestDto build() {
            return new ReservationRequestDto(name, date, reservationTimeRequestDto, reservationThemeRequestDto);
        }

    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public ReservationTimeRequestDto getReservationTimeRequestDto() {
        return reservationTimeRequestDto;
    }

    public ReservationThemeRequestDto getReservationThemeRequestDto() {
        return reservationThemeRequestDto;
    }
}
