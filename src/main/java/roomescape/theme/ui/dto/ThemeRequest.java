package roomescape.theme.ui.dto;

import jakarta.validation.constraints.NotBlank;

public record ThemeRequest (
    @NotBlank String name,
    @NotBlank String description,
    @NotBlank String thumbnail
){
    public static ThemeRequest of(String name, String description, String thumbnail) {
        return new ThemeRequest(name, description, thumbnail);
    }
}
