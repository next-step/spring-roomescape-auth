package roomescape.controller.dto;

public record ReservationAdminRequest(String name, String date, long timeId, long themeId, long memberId) {
}
