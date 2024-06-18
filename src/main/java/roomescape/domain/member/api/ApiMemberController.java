package roomescape.domain.member.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.service.MemberService;
import roomescape.domain.member.service.dto.MemberLoginRequest;

@RestController
@RequestMapping("/login")
public class ApiMemberController {

    public ApiMemberController(MemberService memberService) {
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
}
