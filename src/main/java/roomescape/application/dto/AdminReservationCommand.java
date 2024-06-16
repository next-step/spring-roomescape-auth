package roomescape.application.dto;

public record AdminReservationCommand(String date, Long themeId, Long timeId, Long memberId) {

}
