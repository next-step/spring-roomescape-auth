package roomescape.theme.ui;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.ui.dto.ThemeRequest;
import roomescape.theme.ui.dto.ThemeResponse;
import roomescape.theme.application.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> readAll() {
        List<ThemeResponse> themes = themeService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(themes);
    }

    @GetMapping("{id}")
    public ResponseEntity<ThemeResponse> readOne(@PathVariable Long id) {
        ThemeResponse theme = themeService.findOne(id);
        return ResponseEntity.status(HttpStatus.OK).body(theme);
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> create(@RequestBody @Valid ThemeRequest request) {
        long themeId = themeService.add(request);
        ThemeResponse theme = themeService.findOne(themeId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/themes/" + theme.getId()))
                .body(theme);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
