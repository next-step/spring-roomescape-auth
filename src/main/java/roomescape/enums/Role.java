package roomescape.enums;

import java.util.EnumSet;

public enum Role {
    ADMIN, USER;

    public static Role from(String role) {
        return EnumSet.allOf(Role.class).stream()
                      .filter(r -> r.name().equals(role))
                      .findFirst()
                      .orElseThrow(() -> new IllegalArgumentException("해당 권한은 없음"));
    }
}
