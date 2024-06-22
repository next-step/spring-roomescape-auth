package roomescape.domain.member.api.login;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.argumentResolver.annotation.Login;
import roomescape.domain.member.domain.Member;
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
    public ResponseEntity<Void> login(@RequestBody MemberLoginRequest memberLoginRequest) {
        String token = memberService.login(memberLoginRequest);
        ResponseCookie responseCookie = ResponseCookie.from(TOKEN, token)
                .httpOnly(true)
                .path("/")
                .maxAge(60)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> loginCheck(@Login Member loginMember) {
        MemberResponse memberResponse = new MemberResponse(loginMember.getId(), loginMember.getName());
        return ResponseEntity.ok().body(memberResponse);
    }
}
