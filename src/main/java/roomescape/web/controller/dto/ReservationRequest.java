package roomescape.web.controller.dto;

public record ReservationRequest(String name, String date, long timeId, long themeId) {
}
