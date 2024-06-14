package roomescape.support.checker;

import org.springframework.util.StringUtils;

public class MemberRequestValidator {

    private static final String EMAIL_FORMAT = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private MemberRequestValidator() {
        throw new UnsupportedOperationException("인스턴스화 할 수 없는 클래스입니다.");
    }

    public static void validateValues(String name, String email, String password) {
        if (!StringUtils.hasText(name) || !StringUtils.hasText(email) || !StringUtils.hasText(password)) {
            String message = String.format("필수 값은 비어 있을 수 없습니다. name = %s, email = %s, password = %s", name, email, password);
            throw new IllegalArgumentException(message);
        }

        if (!email.matches(EMAIL_FORMAT)) {
            throw new IllegalArgumentException("이메일을 다시 확인해 주세요.");
        }
    }
}
