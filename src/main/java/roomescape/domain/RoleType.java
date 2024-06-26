package roomescape.domain;

import java.util.Arrays;
import roomescape.exception.custom.InvalidRoleTypeException;

public enum RoleType {
    MEMBER, ADMIN;

    public static RoleType fromName(String name) {
        return Arrays.stream(RoleType.values())
                .filter(roleType -> roleType.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new InvalidRoleTypeException());
    }

    public boolean isAdmin() {
        return this == RoleType.ADMIN;
    }
}
