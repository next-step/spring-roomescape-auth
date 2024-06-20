package roomescape.domain.member.api.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.member.domain.Member;
import roomescape.domain.member.error.exception.MemberErrorCode;
import roomescape.domain.member.error.exception.MemberException;
import roomescape.domain.member.service.MemberService;
import roomescape.domain.member.service.dto.MemberLoginRequest;
import roomescape.domain.member.service.dto.MemberResponse;

@RestController
@RequestMapping("/login")
public class ApiLoginController {

    private static final String TOKEN = "token";

    public ApiLoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    private final MemberService memberService;


    @PostMapping
    public ResponseEntity<?> login(@RequestBody MemberLoginRequest memberLoginRequest) {
        String token = memberService.login(memberLoginRequest);
        ResponseCookie responseCookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .maxAge(60)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).build();
    }

    @GetMapping("/check")
    public ResponseEntity<?> loginCheck(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        Member member = memberService.findByToken(extractTokenFromCookie(cookies));
        MemberResponse memberResponse = new MemberResponse(member.getName());
        return ResponseEntity.ok().body(memberResponse);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN)) {
                return cookie.getValue();
            }
        }
        throw new MemberException(MemberErrorCode.NO_MEMBER_ERROR);
    }
}
