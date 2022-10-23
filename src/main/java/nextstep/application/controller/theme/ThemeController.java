package nextstep.application.controller;

import java.net.URI;
import java.util.List;
import nextstep.application.service.theme.ThemeCommandService;
import nextstep.application.service.theme.ThemeQueryService;
import nextstep.application.dto.theme.ThemeRequest;
import nextstep.application.dto.theme.ThemeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeCommandService themeCommandService;
    private final ThemeQueryService themeQueryService;

    public ThemeController(
        ThemeCommandService themeCommandService,
        ThemeQueryService themeQueryService
    ) {
        this.themeCommandService = themeCommandService;
        this.themeQueryService = themeQueryService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody ThemeRequest request) {
        Long themeId = themeCommandService.create(request);
        return ResponseEntity.created(URI.create("/themes/" + themeId)).build();
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> checkAll() {
        List<ThemeResponse> responses = themeQueryService.checkAll();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        themeCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
