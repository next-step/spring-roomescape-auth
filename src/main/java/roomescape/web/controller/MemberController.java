package roomescape.web.controller;

import java.net.URI;
import java.util.List;

import roomescape.service.MemberService;
import roomescape.web.controller.dto.MemberRequest;
import roomescape.web.controller.dto.MemberResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
		MemberResponse response = this.memberService.create(request);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(response.id())
			.toUri();

		return ResponseEntity.created(location).body(response);
	}

	@GetMapping
	public ResponseEntity<List<MemberResponse>> findAllMembersViaRoleUser() {
		return ResponseEntity.ok(this.memberService.findAllMembersViaRoleUser());
	}

}
