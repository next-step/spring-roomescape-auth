package roomescape.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 TRUE를 반환한다.")
    void isNotMatchPassword_ReturnTrue() {
        // given
        User user = User.createUser(1L, "어드민", "admin@email.com", "1234");

        // when
        boolean result = user.isNotMatchPassword("123");

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("비밀번호가 일치하면 FALSE를 반환한다.")
    void isNotMatchPassword_ReturnFalse() {
        // given
        User user = User.createUser(1L, "어드민", "admin@email.com", "1234");

        // when
        boolean result = user.isNotMatchPassword("1234");

        // then
        assertThat(result).isFalse();
    }
}
