package roomescape.adapter.out;

import java.util.Objects;

public class ReservationTimeEntity {

  private final Long id;
  private final String startAt;

  public ReservationTimeEntity(Long id, String startAt) {
    this.id = id;
    this.startAt = startAt;
  }

  public Long getId() {
    return id;
  }

  public String getStartAt() {
    return startAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReservationTimeEntity that = (ReservationTimeEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  public static ReservationTimeEntity of(Long id, String startAt) {
    return new ReservationTimeEntity(id, startAt);
  }
}
