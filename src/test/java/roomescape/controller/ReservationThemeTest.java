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
import roomescape.exception.custom.DuplicateThemeException;
import roomescape.exception.custom.ReservationThemeConflictException;
import roomescape.fixture.DateFixture;

@Sql("classpath:table_init.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("예약테마 테스트")
public class ReservationThemeTest {

    private final String NAME = "레벨2 탈출";
    private final String DESCRIPTION = "우테코 레벨2를 탈출하는 내용입니다.";
    private final String THUMBNAIL = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";

    private static final String EMAIL = "test@email.com";
    private static final String MEMBER_NAME = "테스트";
    private static final String PASSWORD = "1234";
    private String token;

    @BeforeEach
    void init() {
        회원가입(EMAIL, PASSWORD, MEMBER_NAME);

        Response response = 로그인(EMAIL, PASSWORD);
        token = response.getCookie("token");
    }

    @Test
    @DisplayName("예약테마를 생성한다.")
    void createReservationTheme() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", NAME);
        params.put("description", DESCRIPTION);
        params.put("thumbnail", THUMBNAIL);

        Response response = 예약테마를_생성한다(params);

        int expectedIdValue = 1;
        response.then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/themes/" + expectedIdValue)
                .body("id", is(expectedIdValue));
    }

    @Test
    @DisplayName("예약테마를 생성할 때 필수값이 없는 경우 에러가 발생한다.")
    void missingRequiredFieldsThrowsErrorOnThemeCreation() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "");
        params.put("description", DESCRIPTION);
        params.put("thumbnail", THUMBNAIL);

        Response response = 예약테마를_생성한다(params);

        String message = "테마이름은 필수 값입니다.";
        response.then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is(message));
    }

    @Test
    @DisplayName("예약테마를 생성할 때 테마이름이 중복인 경우 에러가 발생한다.")
    void createReservationThemeDuplicate() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", NAME);
        params.put("description", DESCRIPTION);
        params.put("thumbnail", THUMBNAIL);

        Response firstCreateResponse = 예약테마를_생성한다(params);

        firstCreateResponse.then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        Response secondCreateResponse = 예약테마를_생성한다(params);

        String message = new DuplicateThemeException().getMessage();
        secondCreateResponse.then().log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", is(message));
    }

    @Test
    @DisplayName("예약테마 목록을 조회한다.")
    void findAllReservationThemes() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", NAME);
        params.put("description", DESCRIPTION);
        params.put("thumbnail", THUMBNAIL);

        예약테마를_생성한다(params);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().get("/themes")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약테마를 삭제한다.")
    void deleteReservationTheme() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", NAME);
        params.put("description", DESCRIPTION);
        params.put("thumbnail", THUMBNAIL);

        예약테마를_생성한다(params);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("예약테마를 삭제할 때 사용중인 예약이 있는 경우 에러가 발생한다.")
    void deleteReservationThemeExistReservation() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", NAME);
        params.put("description", DESCRIPTION);
        params.put("thumbnail", THUMBNAIL);

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

        String message = new ReservationThemeConflictException().getMessage();
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("message", is(message));
    }
}
