package nextstep.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.TokenRequest;
import nextstep.auth.TokenResponse;
import nextstep.member.MemberRequest;
import nextstep.reservation.ReservationRequest;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class Requests {

    public static ExtractableResponse<Response> createReservationRequest(String loginToken, ReservationRequest request) {
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + loginToken)
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/reservations")
            .then().log().all()
            .extract();
    }

    public static String getLoginTokenRequest(TokenRequest tokenRequest) {
        var loginResponse = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequest)
            .when().post("/login/token")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
        return loginResponse.body().as(TokenResponse.class).getAccessToken();
    }

    public static Long createMemberRequest(MemberRequest memberRequest) {
        var memberResponse = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(memberRequest)
            .when().post("/members")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
        String[] memberLocation = memberResponse.header("Location").split("/");
        return Long.parseLong(memberLocation[memberLocation.length - 1]);
    }

    public static Long createSchedule(ScheduleRequest scheduleRequest ) {
        var scheduleResponse = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(scheduleRequest)
            .when().post("/schedules")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
        String[] scheduleLocation = scheduleResponse.header("Location").split("/");
        return Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);
    }

    public static Long createTheme(ThemeRequest themeRequest) {
        var themeResponse = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(themeRequest)
            .when().post("/themes")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
        String[] themeLocation = themeResponse.header("Location").split("/");
        return Long.parseLong(themeLocation[themeLocation.length - 1]);
    }
}
