package roomescape.theme.ui.dto;

import roomescape.theme.domain.entity.Theme;

import java.util.List;

public record ThemeResponse (
    Long id,
    String name,
    String description,
    String thumbnail
){
    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }

    public static List<ThemeResponse> fromThemes(List<Theme> themes) {
        return themes.stream().map(ThemeResponse::from).toList();
    }
}
