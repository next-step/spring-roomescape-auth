package roomescape.reservation.presentation;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.auth.dto.LoginRequest;
import roomescape.reservation.dto.UserReservationCreateRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    private final String date = LocalDate.now().plusDays(1).toString();

    private String accessToken;

    @BeforeEach
    void setUp() {
        LoginRequest loginRequest = new LoginRequest("admin@email.com", "password");

        accessToken = RestAssured.given().log().all()
                .body(loginRequest)
                .contentType(ContentType.JSON)
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");
    }

    @Test
    @DisplayName("모든 예약을 조회한다.")
    void testGetReservations() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("예약을 생성한다.")
    void testCreateReservation() {
        UserReservationCreateRequest request = new UserReservationCreateRequest(date, 1L, 1L);

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("예약을 생성 시 존재하지 않는 시간 ID로 생성하면 예외가 발생한다.")
    void testCreateReservation_InvalidTimeId() {
        UserReservationCreateRequest request = new UserReservationCreateRequest(date, 10L, 1L);

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("예약을 생성 시 존재하지 않는 테마 ID로 생성하면 예외가 발생한다.")
    void testCreateReservation_InvalidThemeId() {
        UserReservationCreateRequest request = new UserReservationCreateRequest(date, 1L, 10L);

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("예약을 생성 시 이미 예약된 시간으로 생성하면 예외가 발생한다.")
    void testCreateReservation_AlreadyExist() {
        UserReservationCreateRequest request = new UserReservationCreateRequest(date, 1L, 1L);

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", accessToken)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("예약을 생성 시 지난 날짜로 생성하면 예외가 발생한다.")
    void testCreateReservation_PastDate() {
        UserReservationCreateRequest request = new UserReservationCreateRequest("2021-07-01", 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", accessToken)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("예약을 생성 시 날짜가 없으면 예외가 발생한다.")
    void testCreateReservation_InvalidDate(String date) {
        UserReservationCreateRequest request = new UserReservationCreateRequest(date, 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", accessToken)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("예약을 취소한다.")
    void testCancelReservation() {
        UserReservationCreateRequest request = new UserReservationCreateRequest(date, 1L, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", accessToken)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        RestAssured.given().log().all()
                .when().delete("/reservations/{reservationId}", 1L)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
