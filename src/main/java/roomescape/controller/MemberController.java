package roomescape.controller;

import roomescape.controller.dto.MemberRequest;
import roomescape.controller.dto.MemberResponse;
import roomescape.service.MemberService;
import roomescape.support.PasswordEncoder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;

	MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping
	public ResponseEntity<MemberResponse> create(@RequestBody MemberRequest request) {
		MemberRequest memberRequest = new MemberRequest(request.name(), request.email(),
				PasswordEncoder.encode(request.password()));
		MemberRequest.validateMember(memberRequest);
		return new ResponseEntity<>(this.memberService.create(memberRequest), HttpStatus.CREATED);
	}

}
