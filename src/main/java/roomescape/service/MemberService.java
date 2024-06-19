package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.MemberRequest;
import roomescape.dto.response.LoginResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.exception.custom.TokenNotFoundException;
import roomescape.exception.custom.UserNotFoundException;
import roomescape.repository.MemberDao;
import roomescape.util.JwtTokenProvider;

@Service
public class MemberService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public MemberService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public String tokenLogin(LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        validateMemberCredentials(email, password);

        return jwtTokenProvider.createToken(email);
    }

    private void validateMemberCredentials(String email, String password) {
        Member member = findByEmail(email);
        member.checkPassword(password);
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

        String email = jwtTokenProvider.getPayload(token);
        Member member = findByEmail(email);

        return new LoginResponse(member.getName());
    }

    private void validateToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new TokenNotFoundException();
        }
        jwtTokenProvider.validateToken(token);
    }

    public MemberResponse signup(MemberRequest memberRequest) {
        // TODO. 중복체크 필요
        // TODO. 비밀번호 암호화 필요
        Member member = memberDao.save(this.convertToEntity(memberRequest));
        return this.convertToResponse(member);
    }

    private Member convertToEntity(MemberRequest request) {
        return new Member(request.getName(), request.getEmail(), request.getPassword());
    }

    private MemberResponse convertToResponse(Member member) {
        return new MemberResponse(member.getId(), member.getName());
    }
}
