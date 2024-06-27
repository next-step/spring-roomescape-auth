package roomescape.reservationtheme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

public class ReservationThemeRequestDto {

    @JsonProperty("themeId")
    private Long themeId;

    @NotEmpty(message = "테마명을 입력 해주세요")
    @JsonProperty("name")
    private String name;

    @NotEmpty(message = "테마에 대한 설명을 입력해주세요")
    @JsonProperty("description")
    private String description;

    @NotEmpty(message = "썸네일 url 을 입력해주세요")
    @JsonProperty("thumbnail")
    private String thumbnail;

    public ReservationThemeRequestDto() {
    }

    public ReservationThemeRequestDto(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public ReservationThemeRequestDto(Long themeId, String name, String description, String thumbnail) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationThemeRequestDto that = (ReservationThemeRequestDto) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "{ " +
                "id=" + themeId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
