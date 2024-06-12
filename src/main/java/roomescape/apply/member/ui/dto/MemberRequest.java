package roomescape.apply.member.ui.dto;

import roomescape.apply.member.domain.MemberRoleName;
import roomescape.support.checker.MemberRequestValidator;

import java.util.Set;

public record MemberRequest(
        String name,
        String email,
        String password,
        Set<String> roleNames
) {

    public MemberRequest {
        MemberRequestValidator.validateValues(name, email, password);
    }

    public Set<String> roleNamesWithDefaultValue() {
        if (this.roleNames == null || this.roleNames.isEmpty()) {
            return Set.of(MemberRoleName.GUEST.getValue());
        }
        return roleNames;
    }

}
