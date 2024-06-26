package roomescape.reservationtheme.domain;

import java.util.Objects;

public class ReservationTheme {

    private Long themeId;
    private String name;
    private String description;
    private String thumbnail;

    public ReservationTheme(Long themeId) {
        this.themeId = themeId;
    }

    public ReservationTheme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public ReservationTheme(Long themeId, String name, String description, String thumbnail) {
        this.themeId = themeId;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static class Builder {

        private Long id;
        private String name;
        private String description;
        private String thumbnail;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder thumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public ReservationTheme build() {
            return new ReservationTheme(id, name, description, thumbnail);
        }

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
        ReservationTheme that = (ReservationTheme) o;
        return Objects.equals(themeId, that.themeId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(themeId);
    }

    @Override
    public String toString() {
        return "ReservationTheme{" +
                "id=" + themeId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }
}
