package roomescape.member.ui;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.member.application.FindMemberService;
import roomescape.member.application.SignUpService;
import roomescape.member.ui.dto.MemberRequest;
import roomescape.member.ui.dto.MemberResponse;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("members")
public class MemberController {
    private final FindMemberService findMemberService;
    private final SignUpService signUpService;

    public MemberController(FindMemberService findMemberService, SignUpService signUpService) {
        this.findMemberService = findMemberService;
        this.signUpService = signUpService;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> readAll() {
        List<MemberResponse> memberResponses = findMemberService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(memberResponses);
    }

    @GetMapping("{id}")
    public ResponseEntity<MemberResponse> readOne(@PathVariable Long id) {
        MemberResponse memberResponse = findMemberService.findOne(id);
        return ResponseEntity.status(HttpStatus.OK).body(memberResponse);
    }

    @PostMapping
    public ResponseEntity<MemberResponse> create(
            @RequestBody @Valid MemberRequest memberRequest,
            @RequestParam(name="role", required = false) String memberRoleName) {
        MemberResponse memberResponse = signUpService.signUp(memberRequest, memberRoleName);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/members/" + memberResponse.id()))
                .body(memberResponse);
    }
}
