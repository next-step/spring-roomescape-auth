package roomescape.adapter.out;

import java.util.Objects;

public class ReservationEntity {

  private final Long id;
  private final String name;
  private final String date;
  private final ReservationTimeEntity time;
  private final ThemeEntity theme;

  public ReservationEntity(Long id, String name, String date, ReservationTimeEntity time, ThemeEntity theme) {
    this.id = id;
    this.name = name;
    this.date = date;
    this.time = time;
    this.theme = theme;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDate() {
    return date;
  }

  public ReservationTimeEntity getTime() {
    return time;
  }

  public ThemeEntity getTheme() {
    return theme;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReservationEntity that = (ReservationEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  public static ReservationEntity of(Long id, String name, String date, ReservationTimeEntity time) {
    return new ReservationEntity(id, name, date, time, null);
  }
}
