package roomescape.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.MemberRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.service.MemberService;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> findAllMembers() {
        return ResponseEntity.ok().body(memberService.findAllMembers());
    }

    @PostMapping
    public ResponseEntity<MemberResponse> signup(@Valid @RequestBody MemberRequest memberRequest) {
        MemberResponse memberResponse = memberService.signup(memberRequest);
        return ResponseEntity.ok().body(memberResponse);
    }
}
