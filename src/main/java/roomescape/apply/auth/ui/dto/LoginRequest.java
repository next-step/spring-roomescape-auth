package roomescape.apply.auth.ui.dto;

import org.springframework.util.StringUtils;

public record LoginRequest(
        String email,
        String password
) {

    public LoginRequest {
        validateValues(email, password);
    }

    private static final String EMAIL_FORMAT = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";


    private static void validateValues(String email, String password) {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) {
            String message = String.format("필수 값은 비어 있을 수 없습니다. email = %s, password = %s", email, password);
            throw new IllegalArgumentException(message);
        }

        if (!email.matches(EMAIL_FORMAT)) {
            throw new IllegalArgumentException("이메일을 다시 확인해 주세요.");
        }
    }

}
