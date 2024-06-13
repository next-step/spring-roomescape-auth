package roomescape.time.presntation;

import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.reservation.dto.ReservationCreateRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerTest {

    @Test
    @DisplayName("시간 추가를 한다.")
    void testCreateReservationTime() {
        Map<String, String> params = Map.of("startAt", "10:00");

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
                .statusCode(HttpStatus.CREATED.value());

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
        Map<String, String> params = Map.of("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all();

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1));
    }

    @Test
    @DisplayName("시간을 삭제한다.")
    void testDeleteReservationTime() {
        Map<String, String> params = Map.of("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all();

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("시간 삭제 시 예약된 시간이면 실패한다.")
    void testDeleteReservationTime_Fail() {
        // ReservationTime 생성
        Map<String, String> param = Map.of("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/times")
                .then().log().all()
                .extract();

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

        // Reservation 생성
        ReservationCreateRequest request = new ReservationCreateRequest("브라운", LocalDate.now().plusDays(1).toString(), 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        // ReservationTime 삭제
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("예약 가능한 시간을 조회한다.")
    void getAvailableTimes() {
        // 시간 생성
        Map<String, String> param1 = Map.of("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param1)
                .when().post("/times")
                .then().log().all();

        Map<String, String> param2 = Map.of("startAt", "12:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param2)
                .when().post("/times")
                .then().log().all();

        // 테마 생성
        Map<String, String> params = Map.of(
                "name", "레벨2 탈출",
                "description", "우테코 레벨2를 탈출하는 내용입니다.",
                "thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all();

        // 테스트
        LocalDate date = LocalDate.of(2021, 10, 1);
        Long themeId = 1L;

        RestAssured.given().log().all()
                .param("date", date.toString())
                .param("themeId", themeId)
                .when().get("/times/available")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2));
    }

    @Test
    @DisplayName("예약 가능한 시간 조회 시 테마를 찾을 수 없으면 실패한다.")
    void getAvailableTimes_Fail() {
        LocalDate date = LocalDate.of(2021, 10, 1);
        Long themeId = 1L;

        RestAssured.given().log().all()
                .param("date", date.toString())
                .param("themeId", themeId)
                .when().get("/times/available")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
