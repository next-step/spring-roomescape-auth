package roomescape.admin.dto;

public record SearchRequest(Long userId, Long themeId, String dateFrom, String dateTo) {
}
