package nextstep.member;

import nextstep.auth.LoginService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {
    private MemberService memberService;
    private LoginService loginService;

    public MemberController(MemberService memberService, LoginService loginService) {
        this.memberService = memberService;
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity createMember(@RequestBody MemberRequest memberRequest) {
        Long id = memberService.create(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @GetMapping("/me")
    public ResponseEntity me(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String credential = authorization.split(" ")[1];
        Long id = loginService.extractPrincipal(credential);
        Member member = memberService.findById(id);
        return ResponseEntity.ok(member);
    }
}
