package roomescape.member.ui;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.member.application.SignUpService;
import roomescape.member.ui.dto.MemberRequest;
import roomescape.member.ui.dto.MemberResponse;

@Controller
@RequestMapping("members")
public class MemberController {
    private final SignUpService signUpService;

    public MemberController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @PostMapping
    public ResponseEntity<MemberResponse> create(@RequestBody @Valid MemberRequest memberRequest) {
        MemberResponse memberResponse = signUpService.signUp(memberRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberResponse);
    }
}
