package roomescape.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Map;

public class ReservationFixture {

    public static Response 예약을_생성한다(Map<String, Object> params, String token) {
        return RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations");
    }

    public static Response 예약을_생성한다_관리자(Map<String, Object> params, String token) {
        return RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations");
    }
}
