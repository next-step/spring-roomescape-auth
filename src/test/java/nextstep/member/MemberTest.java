package nextstep.member;

import static nextstep.member.MemberRole.USER;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @DisplayName("사용자의 비밀번호가 올바르지 않을 경우 예외가 발생한다.")
    @Test
    void validatePassword() {
        Member member = new Member("hyeon9mak", "password123", "최현구", "01011112222", USER);
        assertThatThrownBy(() -> member.checkWrongPassword("틀린비밀번호"))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("사용자의 비밀번호가 올바르지 않습니다.");
    }
}
