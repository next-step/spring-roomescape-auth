package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.User;
import roomescape.dto.auth.AuthCheckResponse;
import roomescape.dto.auth.AuthLoginRequest;
import roomescape.exception.custom.AuthorizationException;
import roomescape.jwt.JwtTokenProvider;
import roomescape.repository.AuthRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach


    @Test
    @DisplayName("로그인 성공시")
    void login() {
        String email = "modric@gmail.com";
        String token = authService.authLogin(new AuthLoginRequest(email, "asdf1234"));
        String findEmail = jwtTokenProvider.getEmailByToken(token);
        User response = authService.findUserFromToken(token);
        org.junit.jupiter.api.Assertions.assertEquals(email, findEmail, "똑같은 이메일");
        assertThat(token).isNotEmpty();
    }

    @Test
    @DisplayName("로그인 실패시")
    void failLogin() {
        String wrongEmail = "modric@gmail.com1111111111";
        org.junit.jupiter.api.Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            authService.authLogin(new AuthLoginRequest(wrongEmail, "asdf1234"));
        });

        String collectEmail = "modric@gmail.com";
        String wrongPassword = "asdf12312312312312312312";
        org.junit.jupiter.api.Assertions.assertThrows(AuthorizationException.class, () -> {
            authService.authLogin(new AuthLoginRequest(collectEmail, wrongPassword));
        });
    }

}