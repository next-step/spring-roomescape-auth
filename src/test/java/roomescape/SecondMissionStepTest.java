package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import roomescape.support.BaseWebApplicationTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class SecondMissionStepTest extends BaseWebApplicationTest {

    @Test
    void page() {
        List<String> pages = List.of("/", "/reservation", "/login", "/signup",
                "/admin", "/admin/theme", "/admin/time", "/admin/reservation");
        for (String page : pages) {
            RestAssured.given().log().all()
                    .when().get(page)
                    .then().log().all()
                    .statusCode(200);
        }
    }

    @Test
    void member() {
        saveMember();

        RestAssured.given().log().all()
                .when().get("/members")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    void login() {
        saveMember();

        Map<String, String> loginParam = new HashMap<>();
        loginParam.put("email", "testing@gmail.com");
        loginParam.put("password", "testPassword");
        Response loginResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginParam)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .cookie("token", notNullValue())
                .extract().response();

        String token = loginResponse.getCookie("token");

        RestAssured.given().log().all()
                .cookie("token", token)
                .when()
                .cookie("token", token)
                .get("/login/check")
                .then().log().all()
                .statusCode(200)
                .body("name", is("어드민"));
    }

    @Test
    void timeAvailable() {
        String token = loginAndGetToken();
        for (String time : List.of("10:00", "12:00", "14:00")) {
            saveAndGetTimeId(time, token);
        }

        String timeId = saveAndGetTimeId("16:00", token);
        String themeId = saveAndGetThemeId(token);
        String date = "2099-12-02";
        saveReservationByTimeIdAndThemeIdAndDate(date, timeId, themeId, token);

        String path = "/times/available?date=" + date + "&themeId=" + themeId;

        Response response = RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(4))
                .extract().response();
        List<Map<String, Object>> expectedData = List.of(
                Map.of("startAt", "10:00", "alreadyBooked", false),
                Map.of("startAt", "12:00", "alreadyBooked", false),
                Map.of("startAt", "14:00", "alreadyBooked", false),
                Map.of("startAt", "16:00", "alreadyBooked", true)
        );

        for (int i = 0; i < expectedData.size(); i++) {
            response.then()
                    .body("[" + i + "].startAt", is(expectedData.get(i).get("startAt")))
                    .body("[" + i + "].alreadyBooked", is(expectedData.get(i).get("alreadyBooked")));
        }
    }

    private static void saveMember() {
        Map<String, String> loginParam = new HashMap<>();
        loginParam.put("email", "master@gmail.com");
        loginParam.put("password", "123");
        Response loginResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginParam)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .cookie("token", notNullValue())
                .extract().response();

        String token = loginResponse.getCookie("token");

        Map<String, Object> memberParams = new HashMap<>();
        memberParams.put("name", "테스터");
        memberParams.put("email", "testing@gmail.com");
        memberParams.put("password", "testPassword");
        memberParams.put("roleNames", List.of("어드민"));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(memberParams)
                .when()
                .cookie("token", token)
                .post("/members")
                .then().log().all()
                .statusCode(201)
                .body("name", is("테스터"));
    }

    private String saveAndGetTimeId(String time, String token) {
        Map<String, String> timeParams = new HashMap<>();
        timeParams.put("startAt", time);
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeParams)
                .cookie("token", token)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract().body().path("id").toString();
    }

    private String saveAndGetThemeId(String token) {
        Map<String, String> themeParams = new HashMap<>();
        themeParams.put("name", "레벨2 탈출");
        themeParams.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        themeParams.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .cookie("token", token)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().body().path("id").toString();
    }

    private void saveReservationByTimeIdAndThemeIdAndDate(String date, String timeId, String themeId, String token) {
        Map<String, String> reservationParams = new HashMap<>();
        reservationParams.put("name", "브라운");
        reservationParams.put("date", date);
        reservationParams.put("timeId", timeId);
        reservationParams.put("themeId", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .cookie("token", token)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    public static String loginAndGetToken() {
        saveMember();

        Map<String, String> loginParam = new HashMap<>();
        loginParam.put("email", "testing@gmail.com");
        loginParam.put("password", "testPassword");
        Response loginResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginParam)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .cookie("token", notNullValue())
                .extract().response();

        return loginResponse.getCookie("token");
    }
}
