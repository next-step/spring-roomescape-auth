package nextstep.member;

import java.util.Arrays;

public enum MemberRole {
    USER, ADMIN;

    public static MemberRole from(String roleName) {
        return Arrays.stream(values())
                .filter(role -> role.name().equalsIgnoreCase(roleName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 권한명 입니다."));
    }

    public boolean isAdmin() {
        return ADMIN.equals(this);
    }
}
