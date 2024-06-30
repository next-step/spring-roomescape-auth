package roomescape.step;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.login.dto.LoginRequest;

public class LoginStep {

    public static String 회원_이메일 = "test@test.com";
    public static String 회원_비밀번호 = "password";
    public static String 관리자_이메일 = "admin@test.com";
    public static String 관리자_비밀번호 = "password";

    public static String 회원_토큰_생성() {
        return 토큰_생성(회원_이메일, 회원_비밀번호);
    }

    public static String 관리자_토큰_생성() {
        return 토큰_생성(관리자_이메일, 관리자_비밀번호);
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
