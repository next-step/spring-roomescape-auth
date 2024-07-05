package roomescape.application.domain.theme.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.application.domain.theme.api.dto.request.CreateThemeRequest;
import roomescape.application.domain.theme.api.dto.response.CreateThemeResponse;
import roomescape.application.domain.theme.service.ThemeService;
import roomescape.application.domain.theme.service.command.DeleteThemeCommand;
import roomescape.domain.theme.Theme;

import java.net.URI;

@RestController
public class ThemeCommandApi {

    private final ThemeService themeService;

    public ThemeCommandApi(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/themes")
    public ResponseEntity<CreateThemeResponse> create(@RequestBody @Valid CreateThemeRequest request) {
        Theme theme = themeService.create(request.toCommand());

        return ResponseEntity
                .created(URI.create(String.format("/themes/%d", theme.getId().id())))
                .body(CreateThemeResponse.from(theme));
    }

    @DeleteMapping("/themes/{themeId}")
    public ResponseEntity<Void> delete(@PathVariable Long themeId) {
        themeService.delete(new DeleteThemeCommand(themeId));

        return ResponseEntity.noContent().build();
    }
}
