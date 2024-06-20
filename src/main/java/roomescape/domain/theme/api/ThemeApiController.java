package roomescape.domain.theme.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.theme.service.dto.ThemeRequest;
import roomescape.domain.theme.service.dto.ThemeResponse;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.theme.service.ThemeService;

import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeApiController {

    private final ThemeService themeService;

    public ThemeApiController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> save(@RequestBody ThemeRequest themeRequest) {
        Theme theme = themeService.save(themeRequest);
        return ResponseEntity.ok().body(new ThemeResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        ));
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<Theme> themes = themeService.findAll();
        List<ThemeResponse> responses = themes.stream().map(theme -> new ThemeResponse(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail())
        ).toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        themeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
