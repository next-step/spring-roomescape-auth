package nextstep.theme;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record ThemeRequest(String name, String desc, int price) {

  public Theme toEntity() {
    return new Theme(this.name, this.desc, this.price);
  }
}
