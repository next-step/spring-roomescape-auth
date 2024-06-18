package roomescape;

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
public class ReservationTest {

    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String TIME_ID = "timeId";
    private static final String THEME_ID = "themeId";
    private static final String START_AT = "startAt";
    private static final String DESCRIPTION = "description";
    private static final String THUMBNAIL = "thumbnail";
    private static final String CURRENT_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private static final String REQUEST_TIME = LocalTime.now().plusMinutes(5L).format(DateTimeFormatter.ofPattern("HH:mm"));


    @BeforeEach
    void 테마와_시간을_추가한다() {

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

        //given
        Map<String, Object> time = new HashMap<>();
        time.put(DATE, CURRENT_DATE);
        time.put(START_AT, REQUEST_TIME);
        time.put(THEME_ID, 1L);

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
    }

    @Test
    void 예약_관리_페이지를_랜더링한다() {

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    void 예약을_등록한다() {

        //given
        Map<String, Object> reservation = new HashMap<>();
        reservation.put(NAME, "johnPark");
        reservation.put(DATE, CURRENT_DATE);
        reservation.put(TIME_ID, 1L);
        reservation.put(THEME_ID, 1L);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong(ID)).isEqualTo(1L);
    }

    @Test
    void 에약을_조회한다() {

        //given
        예약을_등록한다();

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("$").size()).isEqualTo(1);
    }

    @Test
    void 예약을_삭제한다() {

        //given
        예약을_등록한다();

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
