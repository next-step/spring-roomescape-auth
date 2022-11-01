package nextstep.member;

import nextstep.auth.LoginMember;
import nextstep.auth.LoginInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest memberRequest) {
        final Long id = memberService.create(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> me(@LoginMember LoginInfo loginInfo) {
        final Member member = memberService.findByLoginInfo(loginInfo);
        return ResponseEntity.ok(MemberResponse.of(member));
    }
}
