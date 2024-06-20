package roomescape.domain.theme.domain;

import static roomescape.utils.FormatCheckUtil.*;

public class Theme {

    private Long id;
    private String name;
    private String description;
    private String thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        validationCheck(name, description, thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    private static void validationCheck(String name, String description, String thumbnail) {
        themeNameFormatCheck(name);
        themeDescriptionCheck(description);
        themeThumbnailFormatCheck(thumbnail);
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
}
