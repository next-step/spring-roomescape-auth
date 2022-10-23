package nextstep.app.web.member;

import nextstep.app.web.member.adapter.in.MemberService;
import nextstep.core.member.in.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/members")
@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody MemberRegisterWebRequest request) {
        MemberResponse member = memberService.register(request.to());
        return ResponseEntity
                .created(URI.create("/members/" + member.getId()))
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberWebResponse> me() {
        Long id = 1L;
        MemberResponse member = memberService.login(id);
        return ResponseEntity.ok(MemberWebResponse.from(member));
    }
}
