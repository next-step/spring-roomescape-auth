package roomescape.domain.theme;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.entities.Theme;
import roomescape.dto.ThemeAddRequestDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/themes")
public class ThemeController {
  final private ThemeService themeService;

  @GetMapping
  public ResponseEntity<List<Theme>> findAllThemes(){
    List<Theme> themes = themeService.findAllThemes();
    return ResponseEntity.ok().body(themes);
  }

  @PostMapping
  public ResponseEntity<Theme> addTheme(@RequestBody ThemeAddRequestDto themeAddRequestDto){
    return ResponseEntity.ok().body(themeService.save(themeAddRequestDto));
  }

  @DeleteMapping("/{id}")
  public void deleteTheme(@PathVariable("id") Long id){
    themeService.deleteThemeById(id);
  }
}
