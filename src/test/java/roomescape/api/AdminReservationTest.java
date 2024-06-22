package roomescape.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminReservationTest {

    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String TIME_ID = "timeId";
    private static final String THEME_ID = "themeId";
    private static final String MEMBER_ID = "memberId";
    private static final String START_AT = "startAt";
    private static final String DESCRIPTION = "description";
    private static final String THUMBNAIL = "thumbnail";
    private static final String CURRENT_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private static final String REQUEST_TIME = LocalTime.now().plusMinutes(5L).format(DateTimeFormatter.ofPattern("HH:mm"));
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String TOKEN = "token";
    private static final String ROLE = "role";
    private static String token = null;
    private Long themeId = null;
    private Long timeId = null;
    private Long memberId = null;

    @BeforeEach
    void 테마와_시간을_추가하고_회원가입을_하고_로그인한_뒤_관리자_권한을_받는다() {

        /* 테마 추가 */
        //given
        Map<String, Object> theme = new HashMap<>();
        theme.put(NAME, "무시무시한 공포 테마");
        theme.put(DESCRIPTION, "오싹");
        theme.put(THUMBNAIL, "www.youtube.com/boorownie");

        //when
        ExtractableResponse<Response> themeResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/themes")
                .then().log().all()
                .extract();

        //then
        assertThat(themeResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(themeResponse.jsonPath().getLong(ID)).isEqualTo(1L);
        this.themeId = themeResponse.jsonPath().getLong(ID);

        /* 예약시간 추가 */
        //given
        Map<String, Object> time = new HashMap<>();
        time.put(DATE, CURRENT_DATE);
        time.put(START_AT, REQUEST_TIME);
        time.put(THEME_ID, this.themeId);

        //when
        ExtractableResponse<Response> timeResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/times")
                .then().log().all()
                .extract();

        //then
        assertThat(timeResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(timeResponse.jsonPath().getLong(ID)).isEqualTo(1);
        this.timeId = timeResponse.jsonPath().getLong(ID);

        /* 회원가입 */
        //given
        Map<String, Object> member = new HashMap<>();
        member.put(EMAIL, "test@naver.com");
        member.put(PASSWORD, "password123");
        member.put(NAME, "박민욱");

        //when
        ExtractableResponse<Response> memberResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(member)
                .when().post("/members")
                .then().log().all()
                .extract();

        //then
        assertThat(memberResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(memberResponse.jsonPath().getString(NAME)).isEqualTo("박민욱");
        this.memberId = memberResponse.jsonPath().getLong(ID);

        /* 로그인 */
        Map<String, Object> loginMember = new HashMap<>();
        loginMember.put(EMAIL, "test@naver.com");
        loginMember.put(PASSWORD, "password123");

        //when
        ExtractableResponse<Response> loginResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginMember)
                .when().post("/login")
                .then().log().all()
                .extract();

        //then
        assertThat(loginResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(loginResponse.cookie(TOKEN)).isNotNull();
        token = loginResponse.cookie(TOKEN);

        /* 관리자 권한 받기 */
        //when
        ExtractableResponse<Response> updateAdminRoleResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(TOKEN, token)
                .when().post("/members/role")
                .then().log().all()
                .extract();

        //then
        assertThat(updateAdminRoleResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(updateAdminRoleResponse.jsonPath().getString(ROLE)).isEqualTo("ADMIN");
    }

    @Test
    void 관리자는_임의로_사용자를_지정해서_예약을_등록할_수_있다() {

        //given
        Map<String, Object> reservation = new HashMap<>();
        reservation.put(DATE, CURRENT_DATE);
        reservation.put(TIME_ID, this.timeId);
        reservation.put(MEMBER_ID, this.themeId);
        reservation.put(THEME_ID, this.memberId);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(TOKEN, token)
                .body(reservation)
                .when().post("admin/reservations")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong(ID)).isEqualTo(1L);
    }
}
