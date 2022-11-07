package nextstep.theme;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/themes")
public class ThemeController {

  private final ThemeService themeService;

  public ThemeController(ThemeService themeService) {
    this.themeService = themeService;
  }

  @PostMapping("/admin")
  public ResponseEntity<Void> createTheme(@RequestBody ThemeRequest themeRequest) {
    Long id = themeService.create(themeRequest);
    return ResponseEntity.created(URI.create("/themes/" + id)).build();
  }

  @GetMapping
  public ResponseEntity<List<ThemeResponse>> showThemes() {
    List<ThemeResponse> results = themeService.findAll();
    return ResponseEntity.ok().body(results);
  }

  @DeleteMapping("/{id}/admin")
  public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
    themeService.delete(id);

    return ResponseEntity.noContent().build();
  }
}
