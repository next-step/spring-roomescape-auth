package roomescape.apply.member.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public record MemberRoleNames(Set<MemberRoleName> roleNames) {

    private static final String DELIMITER = ",";

    public static MemberRoleNames of(Set<MemberRoleName> rolesInMember) {
        return new MemberRoleNames(rolesInMember);
    }

    public static Set<MemberRoleName> getMemberRolesByRoleNames(String roleNames) {
        String[] roleNameValues = roleNames.split(DELIMITER);
        if (roleNameValues.length == 0) {
            return Collections.emptySet();
        }

        return Arrays.stream(roleNameValues).map(MemberRoleName::findRoleByValue).collect(toSet());
    }

    public String getJoinedNames() {
        Set<String> nameValues = this.roleNames.stream().map(MemberRoleName::getValue).collect(toSet());
        return String.join(DELIMITER, nameValues);
    }

}
