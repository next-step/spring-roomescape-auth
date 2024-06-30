package roomescape.test;

import static roomescape.step.LoginStep.관리자_토큰_생성;
import static roomescape.step.LoginStep.회원_토큰_생성;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@DisplayName("페이지 접속 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PageTest {

    @Test
    void 메인_페이지_접속_성공() {
        RestAssured.given().log().all()
            .when().get("/")
            .then().log().all()
            .statusCode(200);
    }

    @Test
    void 어드민_페이지_접속_성공() {
        RestAssured.given().log().all()
            .cookie("token", 관리자_토큰_생성())
            .when().get("/admin/reservation")
            .then().log().all()
            .statusCode(200);
    }

    @Test
    void 관리자_외_어드민_페이지_접속_실패() {
        RestAssured.given().log().all()
            .cookie("token", 회원_토큰_생성())
            .when().get("/admin/reservation")
            .then().log().all()
            .statusCode(401);
    }
}
