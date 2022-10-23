package nextstep.application.service.auth;

import java.util.List;
import nextstep.application.dto.auth.AuthRequest;
import nextstep.application.dto.auth.AuthResponse;
import nextstep.application.dto.member.MemberResponse;
import nextstep.application.service.member.MemberQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AuthCommandService {

    private final MemberQueryService memberQueryService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthCommandService(
        MemberQueryService memberQueryService,
        JwtTokenProvider jwtTokenProvider
    ) {
        this.memberQueryService = memberQueryService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse login(AuthRequest request) {
        MemberResponse memberResponse = memberQueryService.findBy(request.getUsername());

        String principal = memberResponse.getId().toString();
        List<String> roles = List.of(memberResponse.getRole().name());

        return toAuthResponse(jwtTokenProvider.createToken(principal, roles));
    }

    private AuthResponse toAuthResponse(String token) {
        return new AuthResponse(token);
    }
}
