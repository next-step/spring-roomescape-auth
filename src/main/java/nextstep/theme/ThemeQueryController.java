package nextstep.theme;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/themes")
public class ThemeQueryController {

    private ThemeService themeService;

    public ThemeQueryController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> showThemes() {
        List<ThemeResponse> results = themeService.findAll();
        return ResponseEntity.ok().body(results);
    }
}
