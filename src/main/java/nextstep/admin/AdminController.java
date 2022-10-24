package nextstep.admin;

import nextstep.auth.AuthenticationPrincipal;
import nextstep.member.Member;
import nextstep.theme.ThemeRequest;
import nextstep.theme.ThemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ThemeService themeService;

    public AdminController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/themes")
    public ResponseEntity<Void> createTheme(@AuthenticationPrincipal Member adminMember, @RequestBody ThemeRequest request) {
        Long id = themeService.create(request);
        return ResponseEntity.created(URI.create("/themes/" + id)).build();
    }
}
