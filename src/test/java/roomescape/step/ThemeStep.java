package roomescape.step;

import static roomescape.step.LoginStep.관리자_토큰_생성;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import roomescape.theme.dto.ThemeRequest;

public class ThemeStep {

    public static ExtractableResponse<Response> 테마_등록(String token, ThemeRequest request) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .cookie("token", token)
            .body(request)
            .when().post("/themes")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 테마_등록(ThemeRequest request) {
        return 테마_등록(관리자_토큰_생성(), request);
    }
}
