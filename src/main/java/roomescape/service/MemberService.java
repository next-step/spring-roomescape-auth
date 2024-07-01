package roomescape.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.domain.RoleType;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.MemberRequest;
import roomescape.dto.response.LoginResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.exception.custom.DuplicateMemberException;
import roomescape.exception.custom.PasswordMismatchException;
import roomescape.exception.custom.RoleNotFoundException;
import roomescape.exception.custom.TokenNotFoundException;
import roomescape.exception.custom.UserNotFoundException;
import roomescape.repository.MemberDao;
import roomescape.repository.RoleDao;
import roomescape.util.JwtTokenProvider;

@Service
public class MemberService {
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberDao memberDao;
    private final RoleDao roleDao;

    public MemberService(JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, MemberDao memberDao,
                         RoleDao roleDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.memberDao = memberDao;
        this.roleDao = roleDao;
    }

    public String tokenLogin(LoginRequest request) {
        String email = request.getEmail();
        Member member = findByEmail(request.getEmail());

        validateMemberCredentials(member, request.getPassword());

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", member.getName());
        extraClaims.put("role", member.getRole().getName());

        return jwtTokenProvider.createToken(email, extraClaims);
    }

    private void validateMemberCredentials(Member member, String password) {
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new PasswordMismatchException();
        }
    }

    public Member findByEmail(String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    public Member findById(Long id) {
        return memberDao.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public List<MemberResponse> findAllMembers() {
        List<Member> members = memberDao.findAllMembers();
        return members.stream()
                .map(member -> new MemberResponse(member.getId(), member.getName()))
                .toList();
    }

    public LoginResponse loginCheck(String token) {
        validateToken(token);

        String email = jwtTokenProvider.getSubject(token);
        Member member = findByEmail(email);

        return new LoginResponse(member.getName());
    }

    private void validateToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new TokenNotFoundException();
        }
    }

    public MemberResponse signup(MemberRequest memberRequest) {
        validateSignupInformation(memberRequest);

        Member member = memberDao.save(this.convertToEntity(memberRequest));

        return this.convertToResponse(member);
    }

    private void validateSignupInformation(MemberRequest memberRequest) {
        if (memberDao.findByEmail(memberRequest.getEmail()).isPresent()) {
            throw new DuplicateMemberException();
        }
    }

    private Member convertToEntity(MemberRequest request) {
        String password = passwordEncoder.encode(request.getPassword());
        Role role = roleDao.findByName(RoleType.MEMBER.name())
                .orElseThrow(() -> new RoleNotFoundException());

        return new Member(request.getName(), request.getEmail(), password, role);
    }

    private MemberResponse convertToResponse(Member member) {
        return new MemberResponse(member.getId(), member.getName());
    }
}
