package roomescape.apply.member.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.apply.member.application.MemberAdder;
import roomescape.apply.member.application.MemberFinder;
import roomescape.apply.member.ui.dto.MemberRequest;
import roomescape.apply.member.ui.dto.MemberResponse;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberAdder memberAdder;
    private final MemberFinder memberFinder;

    public MemberController(MemberAdder memberAdder, MemberFinder memberFinder) {
        this.memberAdder = memberAdder;
        this.memberFinder = memberFinder;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getMembers() {
        return ResponseEntity.ok(memberFinder.findAll());
    }

    @PostMapping
    public ResponseEntity<MemberResponse> addMember(@RequestBody MemberRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(memberAdder.addNewMember(request));
    }

}
