package roomescape.time.presntation;

import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
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
class ReservationTimeControllerTest {

    @BeforeEach
    void setUp() {
        Map<String, String> params = Map.of("startAt", "15:40");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all();
    }

    @Test
    @DisplayName("시간 추가를 한다.")
    void testCreateReservationTime() {
        Map<String, String> params = Map.of("startAt", "13:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("시간 추가 시 잘못된 시간 형식이면 실패한다.")
    void testCreateReservationTime_InvalidTimeFormat() {
        Map<String, String> params = Map.of("startAt", "10.00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("시간 추가 시 이미 존재하는 시간이면 실패한다.")
    void testCreateReservationTime_AlreadyExistsTime() {
        Map<String, String> params = Map.of("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
    
    @Test
    @DisplayName("모든 시간을 조회한다.")
    void testGetReservationTimes() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(4));
    }

    @Test
    @DisplayName("시간을 삭제한다.")
    void testDeleteReservationTime() {
        RestAssured.given().log().all()
                .when().delete("/times/4")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("시간 삭제 시 예약된 시간이면 실패한다.")
    void testDeleteReservationTime_Fail() {
        // Theme 생성
        Map<String, String> params = Map.of(
                "name", "레벨2 탈출",
                "description", "우테코 레벨2를 탈출하는 내용입니다.",
                "thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .extract();

        // 로그인
        LoginRequest loginRequest = new LoginRequest("admin@email.com", "password");

        String accessToken = RestAssured.given().log().all()
                .body(loginRequest)
                .contentType(ContentType.JSON)
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("예약 가능한 시간을 조회한다.")
    void getAvailableTimes() {
        LocalDate date = LocalDate.of(2021, 10, 1);
        Long themeId = 1L;

        RestAssured.given().log().all()
                .param("date", date.toString())
                .param("themeId", themeId)
                .when().get("/times/available")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(4));
    }

    @Test
    @DisplayName("예약 가능한 시간 조회 시 테마를 찾을 수 없으면 실패한다.")
    void getAvailableTimes_Fail() {
        LocalDate date = LocalDate.of(2021, 10, 1);
        Long themeId = 7L;

        RestAssured.given().log().all()
                .param("date", date.toString())
                .param("themeId", themeId)
                .when().get("/times/available")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
