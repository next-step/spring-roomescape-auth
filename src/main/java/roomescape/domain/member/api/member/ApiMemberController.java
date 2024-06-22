package roomescape.domain.member.api.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.argumentResolver.annotation.Login;
import roomescape.domain.member.domain.Member;
import roomescape.domain.member.service.MemberService;
import roomescape.domain.member.service.dto.AdminMemberResponse;
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
    public ResponseEntity<MemberResponse> save(@RequestBody MemberRequest memberRequest) {
        Member member = memberService.save(memberRequest);
        return ResponseEntity.ok().body(new MemberResponse(member.getId(), member.getName()));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> findAll() {
        List<Member> members = memberService.findAll();
        List<MemberResponse> memberResponses = members.stream().map(member -> new MemberResponse(member.getId(), member.getName())).toList();
        return ResponseEntity.ok().body(memberResponses);
    }

    @PostMapping("/role")
    public ResponseEntity<AdminMemberResponse> updateAdminRole(@Login Member loginMember) {
        Member member = memberService.updateAdminRole(loginMember.getId());
        return ResponseEntity.ok().body(
                new AdminMemberResponse(
                        member.getId(),
                        member.getName(),
                        member.getRole()));
    }
}
