package roomescape.member.ui;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.member.application.MemberService;
import roomescape.member.application.SignUpService;
import roomescape.member.ui.dto.MemberRequest;
import roomescape.member.ui.dto.MemberResponse;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("members")
public class MemberController {
    private final MemberService memberService;
    private final SignUpService signUpService;

    public MemberController(MemberService memberService, SignUpService signUpService) {
        this.memberService = memberService;
        this.signUpService = signUpService;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> readAll() {
        List<MemberResponse> memberResponses = memberService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(memberResponses);
    }

    @GetMapping("{id}")
    public ResponseEntity<MemberResponse> read(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.findOne(id);
        return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
    }

    @PostMapping
    public ResponseEntity<MemberResponse> create(@RequestBody @Valid MemberRequest memberRequest) {
        MemberResponse memberResponse = signUpService.signUp(memberRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/members/" + memberResponse.id()))
                .body(memberResponse);
    }
}
