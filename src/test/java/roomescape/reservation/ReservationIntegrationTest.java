package roomescape.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservationtheme.dto.ReservationThemeRequestDto;
import roomescape.reservationtime.dto.ReservationTimeRequestDto;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationIntegrationTest {

    private ReservationTimeRequestDto reservationTimeRequestDto;
    private ReservationThemeRequestDto reservationThemeRequestDto;
    private ReservationRequestDto reservationRequestDto;

    @BeforeEach
    void setUp() {
        reservationTimeRequestDto = new ReservationTimeRequestDto(1L, "15:40");
        reservationThemeRequestDto = new ReservationThemeRequestDto(
                1L, "테마1", "설명1", "썸네일1");
        reservationRequestDto = new ReservationRequestDto(
                "브라운", "2025-08-05", reservationTimeRequestDto, reservationThemeRequestDto);
        // 시간 등록
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTimeRequestDto)
                .when().post("/times")
                .then().log().all()
                .statusCode(200)
                .body("id", is(1));

        // 테마 등록
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationThemeRequestDto)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));
    }

    @DisplayName("저장된 시간과 테마를 기준으로 예약을 등록 및 조회합니다.")
    @Test
    void reservationCreate() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequestDto)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("저장된 예약을 삭제합니다.")
    @Test
    void reservationDelete(){
        // 예약 저장
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequestDto)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("id", is(1));
        // 예약 삭제
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(200);

        // 예약 조회
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }
}
