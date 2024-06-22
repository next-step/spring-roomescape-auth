package roomescape;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.auth.application.JwtTokenProvider;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JwtTokenTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("JwtTokenProvider - createToken() and extractMemberId()")
    void 토큰_생성_및_id_반환() {
        Long memberId = 1L;

        String token = jwtTokenProvider.createToken(memberId);
        Long memberIdFromToken = jwtTokenProvider.extractMemberId(token);

        assertThat(memberIdFromToken).isEqualTo(memberId);
    }
}
