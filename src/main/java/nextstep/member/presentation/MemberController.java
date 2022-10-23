package nextstep.member.presentation;

import nextstep.auth.resolver.AuthenticationPrincipal;
import nextstep.auth.resolver.LoginUser;
import nextstep.member.MemberService;
import nextstep.member.presentation.dto.CreateMemberRequest;
import nextstep.member.presentation.dto.MemberInfoResponse;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity createMember(@RequestBody CreateMemberRequest createMemberRequest) {
        Long id = memberService.create(createMemberRequest);
        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal LoginUser loginUser) {
        return loginUser.act(ResponseEntity.class)
            .ifAnonymous(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())
            .ifUser(uuid -> ResponseEntity.ok(MemberInfoResponse.from(memberService.findByUuid(uuid))))
            .getResult();
    }
}
