package roomescape.step;

import static roomescape.member.initializer.MemberInitializer.DUMMY_ADMIN_EMAIL;
import static roomescape.member.initializer.MemberInitializer.DUMMY_ADMIN_PASSWORD;
import static roomescape.member.initializer.MemberInitializer.DUMMY_USER_EMAIL;
import static roomescape.member.initializer.MemberInitializer.DUMMY_USER_PASSWORD;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.login.dto.LoginRequest;

public class LoginStep {

    public static String 회원_토큰_생성() {
        return 토큰_생성(DUMMY_USER_EMAIL, DUMMY_USER_PASSWORD);
    }

    public static String 관리자_토큰_생성() {
        return 토큰_생성(DUMMY_ADMIN_EMAIL, DUMMY_ADMIN_PASSWORD);
    }

    public static String 토큰_생성(String email, String password) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(new LoginRequest(email, password))
            .when().post("/login")
            .then().log().all().extract()
            .cookie("token");
    }
}
