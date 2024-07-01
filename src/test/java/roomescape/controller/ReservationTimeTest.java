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
import org.springframework.test.context.jdbc.Sql;
import roomescape.exception.custom.DuplicateTimeException;
import roomescape.exception.custom.ReservationTimeConflictException;
import roomescape.fixture.DateFixture;

@Sql("classpath:table_init.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("예약시간 테스트")
public class ReservationTimeTest {

    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "1234";
    private static final String NAME = "테스트";
    private String token;

    @BeforeEach
    void init() {
        회원가입(EMAIL, PASSWORD, NAME);

        Response response = 로그인(EMAIL, PASSWORD);
        token = response.getCookie("token");
    }

    @Test
    @DisplayName("예약시간을 생성한다.")
    void createReservationTime() {
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", "10:00");

        Response response = 예약시간을_생성한다(params);
        response.then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(1));
    }

    @Test
    @DisplayName("예약시간을 생성할 때 필수값이 없으면 에러가 발생한다.")
    void missingRequiredFieldsThrowsErrorOnTimeCreation() {
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", "");

        String message = "시간은 필수 값입니다.";
        Response response = 예약시간을_생성한다(params);
        response.then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is(message));
    }

    @Test
    @DisplayName("예약시간을 생성할 때 유효하지 않은 값을 입력하면 에러가 발생한다.")
    void invalidTimeThrowsErrorOnTimeCreation() {
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", "kk:12");

        String message = "HH:mm 형식이 아닙니다.";
        Response response = 예약시간을_생성한다(params);
        response.then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is(message));
    }

    @Test
    @DisplayName("예약시간을 생성할 때 시간이 중복되면 에러가 발생한다.")
    void createReservationTimeDuplicate() {
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", "10:00");

        Response firstCreateResponse = 예약시간을_생성한다(params);
        firstCreateResponse.then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(1));

        String message = new DuplicateTimeException().getMessage();
        Response secondCreateResponse = 예약시간을_생성한다(params);
        secondCreateResponse.then().log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", is(message));
    }

    @Test
    @DisplayName("예약시간 목록을 조회한다.")
    void findAllReservationTimes() {
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", "10:00");

        예약시간을_생성한다(params);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약시간을 삭제한다.")
    void deleteReservationTime() {
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", "10:00");

        예약시간을_생성한다(params);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("예약시간을 삭제할 때 사용중인 예약이 있는 경우 에러가 발생한다.")
    void deleteReservationTimeExistReservation() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "레벨2 탈출");
        params.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        예약테마를_생성한다(params);

        params.clear();
        params.put("startAt", "10:00");

        예약시간을_생성한다(params);

        params.clear();
        params.put("name", "브라운");
        params.put("date", DateFixture.formatDate("yyyy-MM-dd", 1));
        params.put("timeId", 1L);
        params.put("themeId", 1L);
        예약을_생성한다(params, token);

        String message = new ReservationTimeConflictException().getMessage();
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", is(message));
    }

    @DisplayName("테마의 예약가능 시간 목록을 조회한다.")
    @Test
    void availableTimes() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "레벨2 탈출");
        params.put("description", "우테코 레벨2를 탈출하는 내용입니다.");
        params.put("thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        예약테마를_생성한다(params);

        params.clear();
        params.put("startAt", "10:00");
        예약시간을_생성한다(params);

        params.clear();
        params.put("startAt", "11:00");
        예약시간을_생성한다(params);

        params.clear();
        params.put("name", "브라운");
        String date = DateFixture.formatDate("yyyy-MM-dd", 1);
        params.put("date", date);
        params.put("timeId", 1L);
        params.put("themeId", 1L);
        예약을_생성한다(params, token);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/times/available?date=" + date + "&themeId=1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1));
    }
}
