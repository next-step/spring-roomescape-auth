package roomescape.application.dto;

import roomescape.enums.Role;

public record MemberCommand(String name, String email, String password, Role role) {

}
