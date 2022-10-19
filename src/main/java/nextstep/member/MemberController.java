package nextstep.member;

import nextstep.auth.LoginMember;
import nextstep.auth.LoginService;
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
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest memberRequest) {
        Long id = memberService.create(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @GetMapping("/me")
    public ResponseEntity<Member> me(@LoginMember Member member) {
        Member findMember = memberService.findById(member.getId());
        return ResponseEntity.ok(findMember);
    }
}
