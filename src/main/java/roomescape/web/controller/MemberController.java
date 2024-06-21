package roomescape.web.controller;

import java.util.List;

import roomescape.service.MemberService;
import roomescape.web.controller.dto.MemberRequest;
import roomescape.web.controller.dto.MemberResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
		MemberRequest.validateMember(request);
		return new ResponseEntity<>(this.memberService.create(request), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<MemberResponse>> findAllMembersViaRoleUser() {
		return ResponseEntity.ok(this.memberService.findAllMembersViaRoleUser());
	}

}
