package roomescape.apply.theme.ui;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.apply.auth.application.annotation.NeedMemberRole;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.theme.application.ThemeDeleter;
import roomescape.apply.theme.application.ThemeFinder;
import roomescape.apply.theme.application.ThemeSaver;
import roomescape.apply.theme.ui.dto.ThemeRequest;
import roomescape.apply.theme.ui.dto.ThemeResponse;

import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeSaver themeSaver;
    private final ThemeFinder themeFinder;
    private final ThemeDeleter themeDeleter;

    public ThemeController(ThemeSaver themeSaver, ThemeFinder themeFinder, ThemeDeleter themeDeleter) {
        this.themeSaver = themeSaver;
        this.themeFinder = themeFinder;
        this.themeDeleter = themeDeleter;
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> findAllThemes() {
        List<ThemeResponse> responses = themeFinder.findAll();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @NeedMemberRole(MemberRoleName.ADMIN)
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody ThemeRequest request) {
        ThemeResponse themeResponse = themeSaver.saveThemeBy(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(themeResponse);
    }

    @DeleteMapping("/{id}")
    @NeedMemberRole(MemberRoleName.ADMIN)
    public HttpEntity<Void> deleteTime(@PathVariable("id") long id) {
        themeDeleter.deleteThemeBy(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
