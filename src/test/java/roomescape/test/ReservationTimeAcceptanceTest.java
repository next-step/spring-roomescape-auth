package roomescape.test;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservationTime.dto.ReservationTimeRequest;
import roomescape.theme.dto.ThemeRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.step.LoginStep.관리자_토큰_생성;
import static roomescape.step.LoginStep.회원_토큰_생성;
import static roomescape.step.ReservationStep.예약_등록;
import static roomescape.step.ReservationTimeStep.예약_시간_등록;
import static roomescape.step.ThemeStep.테마_등록;

@DisplayName("예약 시간 관련 api 호출 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeAcceptanceTest {

    private final ReservationTimeRequest request = new ReservationTimeRequest("12:00");

    @Test
    void 예약_시간_등록_성공() {
        ExtractableResponse<Response> response = 예약_시간_등록(request);

        assertThat(response.statusCode()).isEqualTo(201);
    }

    @Test
    void 예약_시간_형식이_다르면_실패() {
        ReservationTimeRequest requestWithWrongFormat = new ReservationTimeRequest("1200");
        ExtractableResponse<Response> response = 예약_시간_등록(requestWithWrongFormat);

        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    void 관리자_외_예약_시간_등록_실패() {
        ExtractableResponse<Response> response = 예약_시간_등록(회원_토큰_생성(), request);

        assertThat(response.statusCode()).isEqualTo(401);
    }

    @Test
    void 예약_시간_조회_성공() {
        예약_시간_등록(request);

        ExtractableResponse<Response> response = 예약_시간_조회();
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getList("id")).hasSize(1);
    }

    @Test
    void 예약_시간_삭제_성공() {
        예약_시간_등록(request);

        ExtractableResponse<Response> response = 예약_시간_삭제(관리자_토큰_생성(), 1L);
        assertThat(response.statusCode()).isEqualTo(204);

        ExtractableResponse<Response> responseAfterDelete = 예약_시간_조회();
        assertThat(responseAfterDelete.statusCode()).isEqualTo(200);
        assertThat(responseAfterDelete.jsonPath().getList("id")).hasSize(0);
    }

    @Test
    void 예약이_존재하는_시간_삭제_실패() {
        예약_시간_등록(request);

        테마_등록(new ThemeRequest("탈출 미션", "탈출하는 내용입니다.", "thumbnail.jpg"));

        예약_등록(new ReservationRequest("2025-08-05", 1L, 1L));

        ExtractableResponse<Response> response = 예약_시간_삭제(관리자_토큰_생성(), 1L);
        assertThat(response.statusCode()).isEqualTo(409);
    }

    @Test
    void 관리자_외_예약_시간_삭제_실패() {
        예약_시간_등록(request);

        ExtractableResponse<Response> response = 예약_시간_삭제(회원_토큰_생성(), 1L);
        assertThat(response.statusCode()).isEqualTo(401);
    }

    @Test
    void 예약_가능_시간_조회() {
        예약_시간_등록(request);

        테마_등록(new ThemeRequest("탈출 미션", "탈출하는 내용입니다.", "thumbnail.jpg"));

        RestAssured.given().log().all()
            .when().get("/times/available?date=9999-12-31&themeId=1")
            .then().log().all()
            .statusCode(200);
    }

    private ExtractableResponse<Response> 예약_시간_삭제(String token, Long id) {
        return RestAssured.given().log().all()
            .cookie("token", token)
            .when().delete("/times/" + id)
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 예약_시간_조회() {
        return RestAssured.given().log().all()
            .when().get("/times")
            .then().log().all()
            .extract();
    }
}
