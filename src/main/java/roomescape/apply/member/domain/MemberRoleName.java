package roomescape.apply.member.domain;

import java.util.Arrays;

public enum MemberRoleName {
    ADMIN("어드민"),
    GUEST("게스트");

    private final String value;

    MemberRoleName(String value) {
        this.value = value;
    }

    public static MemberRoleName findRoleByValue(String roleName) {
        return Arrays.stream(MemberRoleName.values())
                .filter(it -> it.getValue().equals(roleName))
                .findFirst()
                .orElse(MemberRoleName.GUEST);
    }

    public String getValue() {
        return value;
    }
}
