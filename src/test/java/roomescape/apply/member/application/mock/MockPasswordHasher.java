package roomescape.apply.member.application.mock;

import roomescape.apply.auth.application.PasswordHasher;

public class MockPasswordHasher extends PasswordHasher {

    @Override
    public String hashPassword(String password) {
        return password;
    }
}
