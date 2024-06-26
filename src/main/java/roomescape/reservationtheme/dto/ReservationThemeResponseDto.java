package roomescape.reservationtheme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationThemeResponseDto {

    @JsonProperty("themeId")
    private Long themeId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("thumbnail")
    private String thumbnail;

    public ReservationThemeResponseDto(Long themeId, String name, String description, String thumbnail) {
        this.themeId = themeId;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Long getThemeId() {
        return themeId;
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

    @Override
    public String toString() {
        return "{" +
                "themeId=" + themeId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
