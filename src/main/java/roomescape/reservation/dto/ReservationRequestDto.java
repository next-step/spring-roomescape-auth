package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import roomescape.reservationtheme.dto.ReservationThemeRequestDto;
import roomescape.reservationtime.dto.ReservationTimeRequestDto;

public class ReservationRequestDto {
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "예약일자를 입력해주세요")
    @JsonProperty("date")
    private String date;

    @JsonProperty("time")
    private TimeDto timeDto;
    @JsonProperty("theme")
    private ReservationThemeRequestDto reservationThemeRequestDto;

    public ReservationRequestDto(String name, String date, TimeDto timeDto,
                                 ReservationThemeRequestDto reservationThemeRequestDto) {
        this.name = name;
        this.date = date;
        this.timeDto = timeDto;
        this.reservationThemeRequestDto = reservationThemeRequestDto;
    }

    public static class Builder {
        private String name;
        private String date;
        private TimeDto timeDto;
        private ReservationThemeRequestDto reservationThemeRequestDto;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder timeDto(TimeDto timeDto) {
            this.timeDto = timeDto;
            return this;
        }

        public Builder reservationThemeRequestDto(ReservationThemeRequestDto reservationThemeRequestDto) {
            this.reservationThemeRequestDto = reservationThemeRequestDto;
            return this;
        }

        public ReservationRequestDto build() {
            return new ReservationRequestDto(name, date, timeDto, reservationThemeRequestDto);
        }
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public TimeDto getTimeDto() { return timeDto; }

    public ReservationThemeRequestDto getReservationThemeRequestDto() {
        return reservationThemeRequestDto;
    }

    public void assignName(@NotBlank(message = "예약자명을 입력해주세요") String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ReservationRequestDto{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", timeDto=" + timeDto +
                ", reservationThemeRequestDto=" + reservationThemeRequestDto +
                '}';
    }
}
