package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import roomescape.member.dto.MemberResponseDto;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtheme.dto.ReservationThemeResponseDto;
import roomescape.reservationtime.dto.ReservationTimeResponseDto;

public class ReservationResponseDto {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("member")
    private MemberResponseDto memberResponseDto;
    @JsonProperty("date")
    private String date;
    @JsonProperty("time")
    private ReservationTimeResponseDto reservationTimeResponseDto;
    @JsonProperty("theme")
    private ReservationThemeResponseDto reservationThemeResponseDto;

    public ReservationResponseDto(Long id, MemberResponseDto memberResponseDto, String date,
                                  ReservationTimeResponseDto reservationTimeResponseDto,
                                  ReservationThemeResponseDto reservationThemeResponseDto) {
        this.id = id;
        this.memberResponseDto = memberResponseDto;
        this.date = date;
        this.reservationTimeResponseDto = reservationTimeResponseDto;
        this.reservationThemeResponseDto = reservationThemeResponseDto;
    }

    public static class Builder {
        private Long id;
        private MemberResponseDto memberResponseDto;
        private String date;
        private ReservationTimeResponseDto reservationTimeResponseDto;
        private ReservationThemeResponseDto reservationThemeResponseDto;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder memberResponseDto(MemberResponseDto memberResponseDto) {
            this.memberResponseDto = memberResponseDto;
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
            return new ReservationResponseDto(id, memberResponseDto, date, reservationTimeResponseDto, reservationThemeResponseDto);
        }
    }

    public static ReservationResponseDto reservationResponseDtoFromReservation(Reservation reservation) {
        return new Builder()
                .id(reservation.getId())
                .memberResponseDto(new MemberResponseDto(
                        reservation.getName()
                ))
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

    public MemberResponseDto getMemberResponseDto() { return memberResponseDto; }

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
        return "ReservationResponseDto{" +
                "id=" + id +
                ", memberResponseDto=" + memberResponseDto +
                ", date='" + date + '\'' +
                ", reservationTimeResponseDto=" + reservationTimeResponseDto +
                ", reservationThemeResponseDto=" + reservationThemeResponseDto +
                '}';
    }
}
