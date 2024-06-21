package roomescape.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.user.domain.Role;
import roomescape.user.domain.User;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    private User user;
    private String jwt;

    @BeforeEach
    void setUp() {
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        jwtTokenProvider = new JwtTokenProvider(secretKey, 1000L);

        user = new User(1L, "name", "email@email.com", "password", Role.USER);
        jwt = jwtTokenProvider.createJwt(user);
    }

    @Test
    @DisplayName("JWT 토큰을 생성한다.")
    void createJwt() {
        // given
        User user = new User(1L, "name", "email@email.com", "password", Role.USER);

        // when
        String jwt = jwtTokenProvider.createJwt(user);

        // then
        assertThat(jwt).isNotNull();
    }

    @Test
    void 토큰에서_이메일을_추출한다() {
        String extractedEmail = jwtTokenProvider.getEmail(jwt);

        assertThat(extractedEmail).isEqualTo(user.getEmail());
    }

    @Test
    void 토큰에서_유저의_ROLE을_추출한다() {
        Role extractedRole = jwtTokenProvider.getRole(jwt);

        assertThat(extractedRole).isEqualTo(Role.USER);
    }

    @Test
    void 토큰에서_subject를_추출한다() {
        Long extractedUserId = jwtTokenProvider.getUserId(jwt);

        assertThat(extractedUserId).isEqualTo(user.getId());
    }
}
