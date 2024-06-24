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
public class TimeTest {

    private static final String ID = "id";
    private static final String START_AT = "startAt";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String THUMBNAIL = "thumbnail";
    private static final long THEME_VALUE = 1L;
    private static final String CURRENT_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private static final String REQUEST_TIME = LocalTime.now().plusMinutes(5L).format(DateTimeFormatter.ofPattern("HH:mm"));

    @BeforeEach
    void 테마를_추가한다() {

        //given
        Map<String, Object> theme = new HashMap<>();
        theme.put(NAME, "무시무시한 공포 테마");
        theme.put(DESCRIPTION, "오싹");
        theme.put(THUMBNAIL, "www.youtube.com/boorownie");

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/themes")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong(ID)).isEqualTo(1L);
    }

    @Test
    void 시간을_등록한다() {

        //given
        Map<String, Object> time = new HashMap<>();
        time.put(START_AT, REQUEST_TIME);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/times")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getLong(ID)).isEqualTo(1L);
    }

    @Test
    void 시간을_조회한다() {

        //given
        시간을_등록한다();

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/times/available?date=" + CURRENT_DATE + "&themeId=" + THEME_VALUE)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("$").size()).isEqualTo(1);
    }

    @Test
    void 시간을_삭제한다() {

        //given
        시간을_등록한다();

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
