package roomescape.adapter.in.web;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import roomescape.application.dto.MemberCommand;
import roomescape.application.dto.MemberResponse;
import roomescape.application.port.in.LoginUseCase;
import roomescape.application.port.in.MemberUseCase;

@Controller
public class MemberController {

  private final MemberUseCase memberUseCase;

  public MemberController(MemberUseCase memberUseCase) {
    this.memberUseCase = memberUseCase;
  }

  @GetMapping("/reservation")
  public String getMemberReservationPage() {
    return "reservation";
  }

  @ResponseBody
  @GetMapping("/members")
  public ResponseEntity<List<MemberResponse>> getMembers() {
    return ResponseEntity.ok(memberUseCase.findMembers());
  }

  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  @PostMapping("/members")
  public void createMember(@RequestBody MemberCommand memberCommand) {
    memberUseCase.registerMember(memberCommand);
  }

}
