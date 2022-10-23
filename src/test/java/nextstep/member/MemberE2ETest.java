package nextstep.member;

import static nextstep.member.MemberRole.USER;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.auth.TokenRequest;
import nextstep.auth.TokenResponse;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationResponse;
import nextstep.schedule.ScheduleRequest;
import nextstep.theme.ThemeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MemberE2ETest {

    private static final String DATE = "2022-08-11";
    private static final String TIME = "13:00";

    private ReservationRequest request;
    private Long themeId;

    @BeforeEach
    void setUp() {
        String token = login("admin", "admin_password");
        ThemeRequest themeRequest = new ThemeRequest("테마이름", "테마설명", 22000);
        var themeResponse = RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(themeRequest)
            .when().post("/admin/themes")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
        String[] themeLocation = themeResponse.header("Location").split("/");
        themeId = Long.parseLong(themeLocation[themeLocation.length - 1]);

        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, DATE, TIME);
        var scheduleResponse = RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(scheduleRequest)
            .when().post("/admin/schedules")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
        String[] scheduleLocation = scheduleResponse.header("Location").split("/");
        Long scheduleId = Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);

        MemberRequest body = new MemberRequest(
            "username",
            "password",
            "name",
            "010-1234-5678",
            USER
        );
        var memberResponse = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/members")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();

        String[] memberLocation = memberResponse.header("Location").split("/");
        Long memberId = Long.parseLong(memberLocation[memberLocation.length - 1]);

        request = new ReservationRequest(
            scheduleId,
            "최현구"
        );
    }

    @DisplayName("멤버를 생성한다")
    @Test
    public void create() {
        MemberRequest body = new MemberRequest("username", "password", "name", "010-1234-5678", USER);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("로그인 후 내 정보를 조회한다.")
    @Test
    void me() {
        createMember("hyeon9mak", "123123");
        String token = login("hyeon9mak", "123123");

        MemberResponse response = RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .when().get("/members/me")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().as(MemberResponse.class);

        assertThat(response.getUsername()).isEqualTo("hyeon9mak");
    }

    @DisplayName("로그인 후 내 예약 목록을 조회한다.")
    @Test
    void getReservations() {
        Long memberId = createMember("hyeon9mak", "123123");
        String token = login("hyeon9mak", "123123");
        createReservationWithToken(token);

        List<ReservationResponse> responses = RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .when().get("/members/" + memberId + "/reservations")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().jsonPath().getList(".", ReservationResponse.class);

        assertThat(responses).hasSize(1);
    }

    @DisplayName("자신이 아닌 다른 사람의 목록은 조회할 수 없다.")
    @Test
    void nonOwnerReservations() {
        Long memberId = createMember("hyeon9mak", "123123");
        memberId += 1;
        String token = login("hyeon9mak", "123123");
        createReservationWithToken(token);

        RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .when().get("/members/" + memberId + "/reservations")
            .then().log().all()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    private Long createMember(String username, String password) {
        MemberRequest body = new MemberRequest(username, password, "name", "010-1234-5678", USER);
        return Long.valueOf(RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/members")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract().header("Location")
            .split("/members/")[1]);
    }

    private String login(String username, String password) {
        TokenRequest body = new TokenRequest(username, password);
        TokenResponse response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/login")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract().as(TokenResponse.class);

        return response.accessToken;
    }

    private ExtractableResponse<Response> createReservationWithToken(String token) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/reservations")
            .then().log().all()
            .extract();
    }
}
