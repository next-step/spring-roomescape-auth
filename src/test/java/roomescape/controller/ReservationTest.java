package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static roomescape.fixture.AuthFixture.로그인;
import static roomescape.fixture.MemberFixture.회원가입;
import static roomescape.fixture.ReservationFixture.예약을_생성한다;
import static roomescape.fixture.ReservationThemeFixture.예약테마를_생성한다;
import static roomescape.fixture.ReservationTimeFixture.예약시간을_생성한다;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.exception.custom.ExistingReservationException;
import roomescape.exception.custom.InvalidReservationThemeException;
import roomescape.exception.custom.InvalidReservationTimeException;
import roomescape.exception.custom.PastDateReservationException;
import roomescape.fixture.DateFixture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("예약 테스트")
public class ReservationTest {
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "1234";
    private static final String NAME = "테스트";
    private String token;

    @BeforeEach
    void init() {
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", "15:40");

        예약시간을_생성한다(params);

        params.clear();
        params.put("name", "레벨2 탈출");
        params.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        예약테마를_생성한다(params);

        회원가입(EMAIL, PASSWORD, NAME);

        Response response = 로그인(EMAIL, PASSWORD);
        token = response.getCookie("token");
    }

    @Test
    @DisplayName("예약을 생성한다.")
    void createReservation() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", DateFixture.formatDate("yyyy-MM-dd", 1));
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        Response response = 예약을_생성한다(params, token);

        int expectedIdValue = 1;
        response.then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(expectedIdValue));
    }

    @Test
    @DisplayName("예약을 생성할 때 이미 지난 날짜인경우 에러가 발생한다.")
    void createReservationIsDateExpired() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2023-08-05");
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        String message = new PastDateReservationException().getMessage();
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body("message", is(message));
    }

    @Test
    @DisplayName("예약을 생성할 때 필수값이 없으면 에러가 발생한다.")
    void createReservationException() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", "");
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        String message = "날짜는 필수 값입니다.";
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is(message));
    }

    @Test
    @DisplayName("예약을 생성할때 유효하지 않은 날짜를 입력하면 에러가 발생한다.")
    void createReservationDateException() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2023-kk-05");
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        String message = "yyyy-MM-dd 형식이 아닙니다.";
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is(message));
    }

    @Test
    @DisplayName("예약을 생성할때 존재하지 않는 예약시간 아이디를 입력하면 에러가 발생한다.")
    void createReservationEmptyTimeId() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", DateFixture.formatDate("yyyy-MM-dd", 1));
        params.put("timeId", 3L);
        params.put("themeId", 1L);

        String message = new InvalidReservationTimeException().getMessage();
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is(message));
    }

    @Test
    @DisplayName("예약을 생성할때 존재하지 않는 예약테마 아이디를 입력하면 에러가 발생한다.")
    void createReservationEmptyThemeId() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", DateFixture.formatDate("yyyy-MM-dd", 1));
        params.put("timeId", 1L);
        params.put("themeId", 3L);

        String message = new InvalidReservationThemeException().getMessage();
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is(message));
    }

    @Test
    @DisplayName("예약을 생성할 때 중복된 날짜 및 시간인 경우 에러가 발생한다.")
    void createReservationDateAndTimeStartAtDuplicate() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", DateFixture.formatDate("yyyy-MM-dd", 1));
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        String message = new ExistingReservationException().getMessage();
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", is(message));
    }

    @Test
    @DisplayName("예약 목록을 조회한다.")
    void findAllReservations() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", DateFixture.formatDate("yyyy-MM-dd", 1));
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .cookie("token", token)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteReservation() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(0));
    }
}
