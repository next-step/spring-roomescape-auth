package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.BaseWebApplicationTest;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static roomescape.SecondMissionStepTest.loginAndGetToken;


class FirstMissionStepTest extends BaseWebApplicationTest {

    @Test
    void page() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void reservation() {
        String token = loginAndGetToken();
        sendSaveThemeRequest(token);
        sendSaveReservationTimeRequest(token);

        Map<String, String> reservationParams = new HashMap<>();
        reservationParams.put("name", "브라운");
        reservationParams.put("date", "2026-08-05");
        reservationParams.put("timeId", "1");
        reservationParams.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .cookie("token", token)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when()
                .cookie("token", token)
                .delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("클라이언트에서 올바르지 않는 값을 보내면 400 에러를 발송한다.")
    void reservationIllegalArgumentException() {
        String token = loginAndGetToken();
        sendSaveThemeRequest(token);
        sendSaveReservationTimeRequest(token);

        Map<String, String> reservationParams = new HashMap<>();
        reservationParams.put("name", "");
        reservationParams.put("date", "");
        reservationParams.put("timeId", "");
        reservationParams.put("themeId", "");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when()
                .cookie("token", token)
                .post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("JSON parse error: 필수 값은 비어 있을 수 없습니다. date = "));

        Map<String, String> timeParams = new HashMap<>();
        timeParams.put("startAt", "");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeParams)
                .cookie("token", token)
                .when().post("/times")
                .then().log().all()
                .statusCode(400)
                .body("message", is("JSON parse error: 필수 값은 비어 있을 수 없습니다. startAt = "));

        Map<String, String> themeParams = new HashMap<>();
        themeParams.put("name", "");
        themeParams.put("description", "");
        themeParams.put("thumbnail", "");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .cookie("token", token)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400)
                .body("message", is("JSON parse error: 필수 값은 비어 있을 수 없습니다. name = , description = , thumbnail = "));
    }

    @Test
    void reservationTime() {
        String token = loginAndGetToken();
        sendSaveReservationTimeRequest(token);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when()
                .cookie("token", token)
                .delete("/times/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void themes() {
        String token = loginAndGetToken();
        sendSaveThemeRequest(token);

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when()
                .cookie("token", token)
                .delete("/themes/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    public static void sendSaveReservationTimeRequest(String token) {
        Map<String, String> timeParams = new HashMap<>();
        timeParams.put("startAt", "10:00");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(timeParams)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));
    }

    private void sendSaveThemeRequest(String token) {
        Map<String, String> themeParams = new HashMap<>();
        themeParams.put("name", "레벨2 탈출");
        themeParams.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        themeParams.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .cookie("token", token)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));
    }
}
