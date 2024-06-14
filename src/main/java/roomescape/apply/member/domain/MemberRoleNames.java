package roomescape.apply.member.domain;

import java.util.Set;
import java.util.stream.Collectors;

public record MemberRoleNames(Set<MemberRoleName> roleNames) {

    private static final String DELIMITER = ",";

    public static MemberRoleNames of(Set<MemberRoleName> rolesInMember) {
        return new MemberRoleNames(rolesInMember);
    }

    public String getJoinedNames() {
        Set<String> nameValues = this.roleNames.stream().map(MemberRoleName::getValue).collect(Collectors.toSet());
        return String.join(DELIMITER, nameValues);
    }

}
