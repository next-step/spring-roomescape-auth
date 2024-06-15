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
}
