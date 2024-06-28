package roomescape.step;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.login.dto.LoginRequest;

public class LoginStep {
    public static String 회원_토큰_생성() {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(new LoginRequest("test@test.com", "password"))
            .when().post("/login")
            .then().log().all().extract()
            .cookie("token");
    }

    public static String 관리자_토큰_생성() {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(new LoginRequest("admin@test.com", "password"))
            .when().post("/login")
            .then().log().all().extract()
            .cookie("token");
    }
}
