package roomescape.theme.exception;

public class ThemeNotFoundException extends RuntimeException {

    public ThemeNotFoundException() {
        super("해당 테마가 존재하지 않습니다.");
    }
}
