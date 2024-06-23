package roomescape.member.application;

import org.springframework.stereotype.Service;
import roomescape.member.infra.AuthorizationException;
import roomescape.member.infra.MemberRepository;
import roomescape.member.domain.Member;
import roomescape.member.dto.LoginMemberRequestDto;
import roomescape.member.dto.MemberResponseDto;
import roomescape.member.dto.TokenResponseDto;
import roomescape.member.infra.JwtTokenProvider;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvide;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvide, MemberRepository memberRepository) {
        this.jwtTokenProvide = jwtTokenProvide;
        this.memberRepository = memberRepository;
    }

    public TokenResponseDto createToken(LoginMemberRequestDto loginMemberRequestDto) {
        final Member loginMember = new Member(loginMemberRequestDto.getEmail(), loginMemberRequestDto.getPassword());
        checkMember(loginMember);
        Long id = memberRepository.findIdByEmail(loginMember.getEmail());

        String accessToken = jwtTokenProvide.createToken(String.valueOf(id));
        return new TokenResponseDto(accessToken);
    }

    public void checkMember(Member member) {

        boolean existByEmail = memberRepository.isExistByEmail(member);
        if (!existByEmail) {
            throw new AuthorizationException("해당하는 회원 정보가 없습니다.");
        }

        boolean exsitByEmailAndPassword = memberRepository.isExistMemBerByEmailAndPassword(member.getEmail(), member.getPassword());
        if (!exsitByEmailAndPassword) {
            throw new AuthorizationException("해당하는 회원 정보가 없습니다.");
        }
    }

    public MemberResponseDto findMemberName(String token) {
        boolean isTokenExpired = jwtTokenProvide.validateToken(token);
        if (!isTokenExpired) {
            throw new AuthorizationException("만료된 토큰입니다.");
        }

        String id = jwtTokenProvide.extractMemberIdFromToken(token);
        String nameById = memberRepository.findNameById(Long.parseLong(id));
        return new MemberResponseDto(nameById);

    }
}
