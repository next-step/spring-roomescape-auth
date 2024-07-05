package roomescape.auth.error.key;

public class AuthErrorKey {

    private static final String MESSAGE_FORMAT = "[%s : %s] ";

    private final String name;
    private final String value;

    public AuthErrorKey(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String toMessage() {
        return String.format(MESSAGE_FORMAT, this.name, this.value);
    }
}
