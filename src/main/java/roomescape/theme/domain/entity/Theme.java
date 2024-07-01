package roomescape.theme.domain.entity;

import roomescape.theme.ui.dto.ThemeRequest;

public class Theme {
    private Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    private Theme(Long id, String name, String description, String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static Theme of(Long id, String name, String description, String thumbnail) {
        return new Theme(id, name, description, thumbnail);
    }

    public static Theme from(ThemeRequest request) {
        return new Theme(null, request.name(), request.description(), request.thumbnail());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
