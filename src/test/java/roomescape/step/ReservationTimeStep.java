package roomescape.step;

import static roomescape.step.LoginStep.관리자_토큰_생성;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import roomescape.reservationTime.dto.ReservationTimeRequest;

public class ReservationTimeStep {

    public static ExtractableResponse<Response> 예약_시간_등록(String token, ReservationTimeRequest request) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .cookie("token", token)
            .body(request)
            .when().post("/times")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 예약_시간_등록(ReservationTimeRequest request) {
        return 예약_시간_등록(관리자_토큰_생성(), request);
    }
}
