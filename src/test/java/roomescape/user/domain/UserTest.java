package roomescape.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("비밀번호가 일치하면 TRUE를 반환한다.")
    void matchPassword_ReturnTrue() {
        // given
        User user = new User(1L, "admin@email.com", "1234");

        // when
        boolean result = user.matchPassword("1234");

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("비밀번호가 일치하면 FALSE를 반환한다.")
    void matchPassword_ReturnFalse() {
        // given
        User user = new User(1L, "admin@email.com", "1234");

        // when
        boolean result = user.matchPassword("123");

        // then
        assertThat(result).isFalse();
    }
}
