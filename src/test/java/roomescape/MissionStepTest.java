package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservationTheme.dto.ReservationThemeRequestDto;
import roomescape.reservationTime.dto.ReservationTimeRequestDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    @DisplayName("루트 경로('/') 호출 시 200 statusCode를 반환합니다.")
    @Test
    void page() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("시간, 테마 등록 및 예약 등록, 전체조회, 삭제 테스트를 수행합니다.")
    @Test
    void reservation() {
        final ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto((long) 1, "15:40");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTimeRequestDto)
                .when().post("/times")
                .then().log().all()
                .statusCode(200)
                .body("id", is(1));

        final ReservationThemeRequestDto reservationThemeRequestDto = new ReservationThemeRequestDto(
                (long) 1, "테마1", "설명1", "썸네일1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationThemeRequestDto)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        final ReservationRequestDto reservationRequestDto = new ReservationRequestDto(
                "브라운", "2025-08-05", reservationTimeRequestDto, reservationThemeRequestDto);

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

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }
}
