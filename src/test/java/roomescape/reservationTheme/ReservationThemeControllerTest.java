package roomescape.reservationTheme;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservationTheme.dto.ReservationThemeRequestDto;
import roomescape.reservationTheme.dto.ReservationThemeResponseDto;
import roomescape.reservationTheme.infra.ReservationThemeRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationThemeControllerTest {

    private ReservationThemeRepository reservationThemeRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        reservationThemeRepository = new ReservationThemeRepository(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation");
        jdbcTemplate.execute("DROP TABLE IF EXISTS theme");

        jdbcTemplate.execute("""
                CREATE TABLE theme (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            name VARCHAR(255) NOT NULL,
                            description VARCHAR(255) NOT NULL,
                            thumbnail VARCHAR(255) NOT NULL,
                            PRIMARY KEY (id))
                """);

    }

    @DisplayName("모든 테마를 조회할 수 있습니다.")
    @Test
    void getThemes() {
        //given
        final ReservationThemeRequestDto requestDto1 = new ReservationThemeRequestDto("쏘우1", "게임을 시작하지~! (-_-)b", "https://soo1.com");
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestDto1)
                .when().post("/themes")
                .then().log().all().extract().response();

        // when
        final ReservationThemeResponseDto[] reservationThemeResponseDtos = RestAssured.given().log().all()
                .when().get("/themes").then().log().all().extract().as(ReservationThemeResponseDto[].class);

        // then
        assertThat(requestDto1.getName()).isEqualTo(reservationThemeResponseDtos[0].getName());
        assertThat(requestDto1.getDescription()).isEqualTo(reservationThemeResponseDtos[0].getDescription());
    }

    @DisplayName("테마를 저장할 수 있다.")
    @Test
    void createTheme() {

        //given
        final ReservationThemeRequestDto requestDto1 = new ReservationThemeRequestDto("쏘우1",
                "게임을 시작하지~! (-_-)b", "https://soo1.com");

        //when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestDto1)
                .when().post("/themes")
                .then().log().all().extract().response();

        //then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.jsonPath().getString("name")).isEqualTo(requestDto1.getName());
    }

    @DisplayName("테마를 삭제할 수 있습니다.")
    @Test
    void deleteTheme() {
        //given
        final ReservationThemeRequestDto requestDto1 = new ReservationThemeRequestDto("쏘우1",
                "게임을 시작하지~! (-_-)b", "https://soo1.com");
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestDto1)
                .when().post("/themes")
                .then().log().all().extract().response();

        //when
        final Response response2 = RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all().extract().response();

        //then
        assertThat(response2.statusCode()).isEqualTo(204);
    }

    @DisplayName("테마 이름이 null이거나 빈 문자열이면 예외가 발생합니다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createThemeEmptyName(final String name) {
        //given
        final ReservationThemeRequestDto requestDto1 = new ReservationThemeRequestDto(name, "게임을 시작하지~! (-_-)b", "https://soo1.com");

        //when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestDto1)
                .when().post("/themes")
                .then().log().all().extract().response();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo("테마명을 입력 해주세요");
    }

    @DisplayName("테마 설명이 null이거나 빈 문자열이면 예외가 발생합니다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createThemeEmptyDescription(final String description) {
        //given
        final ReservationThemeRequestDto requestDto1 = new ReservationThemeRequestDto("쏘우1", description, "https://soo1.com");

        //when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestDto1)
                .when().post("/themes")
                .then().log().all().extract().response();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("description")).isEqualTo("테마에 대한 설명을 입력해주세요");
    }

    @DisplayName("테마 썸네일이 null이거나 빈 문자열이면 예외가 발생합니다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createThemeEmptyThumbnail(final String thumbnail) {
        //given
        final ReservationThemeRequestDto requestDto1 = new ReservationThemeRequestDto("쏘우1", "게임을 시작하지~! (-_-)b", thumbnail);

        //when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestDto1)
                .when().post("/themes")
                .then().log().all().extract().response();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("thumbnail")).isEqualTo("썸네일 url 을 입력해주세요");
    }
}
