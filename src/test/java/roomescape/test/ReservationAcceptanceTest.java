package roomescape.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservationTime.dto.ReservationTimeRequest;
import roomescape.theme.dto.ThemeRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static roomescape.step.ReservationStep.예약_등록;

@DisplayName("예약 관련 api 호출 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationAcceptanceTest {

    private final ReservationRequest request = new ReservationRequest("2025-08-05", 1L, 1L);

    @BeforeEach
    void 예약_시간_및_테마_등록() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest("12:00");
        ThemeRequest themeRequest = new ThemeRequest("탈출 미션", "탈출하는 내용입니다.", "thumbnail.jpg");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(reservationTimeRequest)
            .when().post("/times")
            .then().log().all()
            .statusCode(201);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(themeRequest)
            .when().post("/themes")
            .then().log().all()
            .statusCode(201)
            .body("id", is(1));
    }

    @Test
    void 예약_등록_성공() {
        ExtractableResponse<Response> response = 예약_등록(request);

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void 관리자용_예약_등록_성공() {
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(new AdminReservationRequest(1L, "2025-08-05", 1L, 1L))
            .when().post("/admin/reservations")
            .then().log().all()
            .statusCode(201);
    }

    @Test
    void 날짜가_과거이면_예약_등록_실패() {
        ReservationRequest requestWithPastDate = new ReservationRequest("2023-08-05", 1L, 1L);
        ExtractableResponse<Response> response = 예약_등록(requestWithPastDate);

        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    void 이미_존재하는_예약_등록_실패() {
        ExtractableResponse<Response> firstResponse = 예약_등록(request);
        assertThat(firstResponse.statusCode()).isEqualTo(200);

        ExtractableResponse<Response> secondResponse = 예약_등록(request);
        assertThat(secondResponse.statusCode()).isEqualTo(409);
    }

    @Test
    void 존재하지_않는_시간의_예약_등록_실패() {
        ReservationRequest requestWithNotExistTime = new ReservationRequest("2025-08-05", 2L,
            1L);
        assertThat(예약_등록(requestWithNotExistTime).statusCode()).isEqualTo(404);
    }

    @Test
    void 예약_조회_성공() {
        예약_등록(request);
        ExtractableResponse<Response> response = 예약_조회();

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void 예약_삭제_성공() {
        예약_등록(request);

        RestAssured.given().log().all()
            .when().delete("/reservations/1")
            .then().log().all()
            .statusCode(204);

        ExtractableResponse<Response> responseAfterDelete = 예약_조회();

        assertThat(responseAfterDelete.statusCode()).isEqualTo(200);
    }

    private ExtractableResponse<Response> 예약_조회() {
        return RestAssured.given().log().all()
            .when().get("/reservations")
            .then().log().all()
            .extract();
    }
}
