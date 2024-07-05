package roomescape.auth.error.key;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AuthErrorKeys {

    private final List<AuthErrorKey> keys;

    public AuthErrorKeys(List<AuthErrorKey> keys) {
        this.keys = keys;
    }

    public static AuthErrorKeys of(AuthErrorKey... keys) {
        return new AuthErrorKeys(new ArrayList<>(Arrays.asList(keys)));
    }

    public String toMessage() {
        return keys.stream()
                .map(AuthErrorKey::toMessage)
                .collect(Collectors.joining(", "));
    }
}
