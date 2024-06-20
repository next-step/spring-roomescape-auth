package roomescape.domain.member.api.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.member.domain.Member;
import roomescape.domain.member.service.MemberService;
import roomescape.domain.member.service.dto.MemberRequest;
import roomescape.domain.member.service.dto.MemberResponse;

import java.util.List;

@RestController
@RequestMapping("/members")
public class ApiMemberController {

    public ApiMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody MemberRequest memberRequest) {
        Member member = memberService.save(memberRequest);
        return ResponseEntity.ok().body(new MemberResponse(member.getName()));
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Member> members = memberService.findAll();
        List<MemberResponse> memberResponses = members.stream().map(member -> new MemberResponse(member.getName())).toList();
        return ResponseEntity.ok().body(memberResponses);
    }
}
