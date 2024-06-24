package roomescape.theme.presentation;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.auth.dto.LoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeControllerTest {

    @Test
    @DisplayName("테마를 생성한다.")
    void testCreateTheme() {
        Map<String, String> params = Map.of(
                "name", "레벨1 탈출",
                "description", "우테코 레벨1를 탈출하는 내용입니다.",
                "thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("테마 목록을 조회한다.")
    void getThemes() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3));
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void deleteTheme() {
        RestAssured.given().log().all()
                .when().delete("/themes/3")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        RestAssured.given().log().all()
                .when().delete("/themes/3")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("테마 삭제 시 테마를 찾을 수 없는 경우 404를 반환한다.")
    void deleteTheme_NotFoundTheme() {
        RestAssured.given().log().all()
                .when().delete("/themes/100")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", is("해당 테마가 존재하지 않습니다."));
    }

    @Test
    void 테마_삭제_시_예약이_존재하면_404를_반환한다() {
        Map<String, String> param = Map.of("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/times")
                .then().log().all();

        LoginRequest loginRequest = new LoginRequest("admin@email.com", "password");

        String accessToken = RestAssured.given().log().all()
                .body(loginRequest)
                .contentType(ContentType.JSON)
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", is("테마에 예약이 있어 삭제할 수 없습니다."));
    }
}
