package roomescape.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.application.JwtTokenProvider;
import roomescape.member.domain.entity.Member;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JwtTokenTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("JwtTokenProvider - createToken() and extractMemberId()")
    void 토큰_생성_및_id_반환() {
        Member member = Member.of(1L, "yeeun", "anna862700@gmail.com", "password");

        String token = jwtTokenProvider.createToken(member);
        Long memberIdFromToken = jwtTokenProvider.extractMemberId(token);

        assertThat(memberIdFromToken).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("JwtTokenProvider - validateToken()")
    void 토큰_유효성_검증() {
        Member member = Member.of(1L, "yeeun", "anna862700@gmail.com", "password");
        String token = jwtTokenProvider.createToken(member);

        boolean isValidOrNot = jwtTokenProvider.validateToken(token);

        assertThat(isValidOrNot).isTrue();
    }
}