package roomescape.login.service;

import roomescape.login.LoginMember;

public interface LoginMemberService {
    LoginMember getLoginMember(String email, String password);
}
