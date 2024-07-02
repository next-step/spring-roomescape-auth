package roomescape.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ThemeAddRequestDto {
  private String name;

  private String description;

  private String thumbnail;

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
