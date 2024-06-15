package roomescape.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        jwtTokenProvider = new JwtTokenProvider(secretKey, 1000L);
    }

    @Test
    @DisplayName("JWT 토큰을 생성한다.")
    void createJwt() {
        // given
        String email = "email@email.com";

        // when
        String jwt = jwtTokenProvider.createJwt(email);

        // then
        assertThat(jwt).isNotNull();
    }

    @Test
    void 토큰에서_이메일을_추출한다() {
        // given
        String email = "email@email.com";
        String jwt = jwtTokenProvider.createJwt(email);

        // when
        String extractedEmail = jwtTokenProvider.getEmail(jwt);

        // then
        assertThat(extractedEmail).isEqualTo(email);
    }

    @Test
    void 토큰이_빈문자열이면_NULL을_반환한다() {
        // given
        String jwt = "";

        // when
        String extractedEmail = jwtTokenProvider.getEmail(jwt);

        // then
        assertThat(extractedEmail).isNull();
    }
}
