package roomescape.application.dto;

public record ReservationCommand(String date, Long themeId, Long timeId, String name) {

}
