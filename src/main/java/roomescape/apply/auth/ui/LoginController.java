package roomescape.apply.auth.ui;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.apply.auth.application.LoginManager;
import roomescape.apply.auth.application.annotation.NeedMemberRole;
import roomescape.apply.auth.ui.dto.LoginCheckResponse;
import roomescape.apply.auth.ui.dto.LoginRequest;
import roomescape.apply.auth.ui.dto.RedirectResponse;
import roomescape.apply.member.application.MemberFinder;
import roomescape.apply.member.domain.MemberRoleName;

@RestController
public class LoginController {

    private final MemberFinder memberFinder;
    private final LoginManager loginTokenManager;

    public LoginController(MemberFinder memberFinder, LoginManager loginTokenManager) {
        this.memberFinder = memberFinder;
        this.loginTokenManager = loginTokenManager;
    }

    @PostMapping("/login")
    public ResponseEntity<RedirectResponse> login(@RequestBody LoginRequest request,
                                                  HttpServletResponse servletResponse
    ) {
        var loginResponse = memberFinder.findByLoginRequest(request);
        loginTokenManager.addTokenToCookie(loginResponse, servletResponse);
        return ResponseEntity.ok(new RedirectResponse(request.redirect()));
    }

    @GetMapping("/login/check")
    @NeedMemberRole({MemberRoleName.ADMIN, MemberRoleName.GUEST})
    public ResponseEntity<LoginCheckResponse> loginCheck(HttpServletRequest servletRequest) {
        LoginCheckResponse response = loginTokenManager.findRoleNameByToken(servletRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<RedirectResponse> logout(HttpServletResponse response
    ) {
        loginTokenManager.removeToken(response);
        return ResponseEntity.ok().build();
    }
}
