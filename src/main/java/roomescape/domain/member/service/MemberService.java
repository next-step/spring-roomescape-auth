package roomescape.domain.member.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.member.domain.Member;
import roomescape.domain.member.domain.repository.MemberRepository;
import roomescape.domain.member.error.exception.MemberErrorCode;
import roomescape.domain.member.error.exception.MemberException;
import roomescape.domain.member.service.dto.MemberLoginRequest;

@Service
public class MemberService {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public String login(MemberLoginRequest memberLoginRequest) {
        Member member = memberRepository.login(memberLoginRequest.getEmail(), memberLoginRequest.getPassword()).orElseThrow(() -> new MemberException(MemberErrorCode.COOKIE_NOT_FOUND_ERROR));
        return Jwts.builder()
                .claim("id", member.getId())
                .claim("email", member.getEmail())
                .claim("name", member.getName())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }
}
