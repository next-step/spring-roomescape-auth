package roomescape.reservation;

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
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.reservation.dto.ReservationResponseDto;
import roomescape.reservation.infra.ReservationRepository;
import roomescape.reservationTheme.domain.ReservationTheme;
import roomescape.reservationTheme.dto.ReservationThemeRequestDto;
import roomescape.reservationTheme.dto.ReservationThemeResponseDto;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.dto.ReservationTimeRequestDto;
import roomescape.reservationTime.dto.ReservationTimeResponseDto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationControllerTest {

    private ReservationRepository reservationRepository;
    private Long time1Id;
    private Long time2Id;
    private Long time3Id;
    private String time1startAt = "";
    private String time2startAt = "";
    private String time3startAt = "";

    private Long theme1Id;
    private String theme1Name;
    private String theme1Thumbnail;
    private String theme1description;
    private Long theme2Id;
    private String theme2Name;
    private String theme2Thumbnail;
    private String theme2description;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        reservationRepository = new ReservationRepository(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation");
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation_time");
        jdbcTemplate.execute("DROP TABLE IF EXISTS theme");

        jdbcTemplate.execute(
                """
                        CREATE TABLE reservation_time (
                            id BIGINT NOT NULL AUTO_INCREMENT, 
                            start_at VARCHAR(255) NOT NULL,
                            PRIMARY KEY (id)
                        )
                        """
        );


        jdbcTemplate.execute("""
                CREATE TABLE theme (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            name VARCHAR(255) NOT NULL,
                            description VARCHAR(255) NOT NULL,
                            thumbnail VARCHAR(255) NOT NULL,
                            PRIMARY KEY (id))
                """);

        jdbcTemplate.execute(
                """
                        CREATE TABLE reservation (
                            id BIGINT NOT NULL AUTO_INCREMENT, 
                            name VARCHAR(255) NOT NULL, 
                            date VARCHAR(255) NOT NULL, 
                            time_id BIGINT,
                            theme_id BIGINT,
                            PRIMARY KEY (id),
                            FOREIGN KEY (time_id) REFERENCES reservation_time (id),
                            FOREIGN KEY (theme_id) REFERENCES theme (id)
                        )
                        """
        );

        final List<Object[]> reservationTimes = Arrays.asList(
                        new ReservationTime("10:00"),
                        new ReservationTime("12:00"),
                        new ReservationTime("14:00"))
                .stream()
                .map(reservationTime -> new Object[]{reservationTime.getStartAt()})
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate("INSERT INTO reservation_time(start_at) VALUES (?)", reservationTimes);

        final Response response1 = RestAssured
                .given().log().all()
                .when().get("/times")
                .then().log().all().extract().response();

        final List<ReservationTimeResponseDto> timeResponseDtos = response1.jsonPath().getList(".", ReservationTimeResponseDto.class);
        time1Id = timeResponseDtos.get(0).getId();
        time2Id = timeResponseDtos.get(1).getId();
        time3Id = timeResponseDtos.get(2).getId();
        time1startAt = timeResponseDtos.get(0).getStartAt();
        time2startAt = timeResponseDtos.get(1).getStartAt();
        time3startAt = timeResponseDtos.get(2).getStartAt();

        final ReservationTheme theme1 = new ReservationTheme("테마1", "설명1", "썸네일1");
        final ReservationTheme theme2 = new ReservationTheme("테마2", "설명2", "썸네일2");
        final List<Object[]> themes = Arrays.asList(theme1, theme2).stream()
                .map(theme -> new Object[]{theme.getName(), theme.getDescription(), theme.getThumbnail()})
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate("INSERT INTO theme(name, description, thumbnail) VALUES (?,?,?)", themes);

        final Response response2 = RestAssured
                .given().log().all()
                .when().get("/themes")
                .then().log().all().extract().response();

        final List<ReservationThemeResponseDto> themeResponseDtos = response2.jsonPath().getList(".", ReservationThemeResponseDto.class);
        theme1Id = themeResponseDtos.get(0).getId();
        theme1Name = themeResponseDtos.get(0).getName();
        theme1Thumbnail = themeResponseDtos.get(0).getThumbnail();
        theme1description = themeResponseDtos.get(0).getDescription();
        theme2Id = themeResponseDtos.get(1).getId();
        theme2Name = themeResponseDtos.get(1).getName();

        final ReservationTime savedTime = new ReservationTime(time1Id, time1startAt);
        final ReservationTheme savedTheme = new ReservationTheme.Builder().id(theme1Id).name(theme1Name).thumbnail(theme1Thumbnail).description(theme1Thumbnail).build();
        final Reservation savedReservation = new Reservation("김준성", "2024-12-25", savedTime, savedTheme);

        final List<Object[]> reservations = Arrays.asList(savedReservation).stream()
                .map(reservation -> new Object[]{
                        reservation.getName(),
                        reservation.getDate(),
                        reservation.getReservationTime().getId(),
                        reservation.getReservationTheme().getId()})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("INSERT INTO reservation(name, date, time_id, theme_id) VALUES (?,?,?,?)", reservations);
    }

    @DisplayName("예약을 생성합니다.")
    @Test
    void createReservation() {
        // given
        final ReservationRequestDto request = new ReservationRequestDto("제이슨", "2025-08-05",
                new ReservationTimeRequestDto(time1Id, time1startAt),
                new ReservationThemeRequestDto(theme1Id));

        // when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all().extract().response();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final ReservationResponseDto responseDto = response.as(ReservationResponseDto.class);
        assertThat(responseDto.getName()).isEqualTo(request.getName());
        assertThat(responseDto.getDate()).isEqualTo(request.getDate());
        assertThat(responseDto.getReservationTimeResponseDto().getStartAt())
                .isEqualTo(request.getReservationTimeRequestDto().getStartAt());
    }

    @DisplayName("전체 예약을 조회 합니다.")
    @Test
    void readReservation() {
        var response = RestAssured
                .given().log().all()
                .when().get("/reservations")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", ReservationResponseDto.class)).hasSize(1);
    }

    @DisplayName("예약을 삭제합니다.")
    @Test
    void deleteReservation() {
        // given
        final ReservationRequestDto request = new ReservationRequestDto("제이슨", "2024-08-05",
                new ReservationTimeRequestDto(time1Id, time1startAt),
                new ReservationThemeRequestDto(theme2Id, theme2Name, theme2description, theme2Thumbnail));

        // when
        final Response response1 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all().extract().response();

        // when
        final Response response2 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().delete("/reservations/" + response1.as(ReservationResponseDto.class).getId())
                .then().log().all().extract().response();

        // then
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("예약자 명이 null이거나 빈 문자열이면 예외가 발생합니다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createReservationEmptyName(final String name) {
        // given
        final ReservationRequestDto request = new ReservationRequestDto(name, "2025-08-05",
                new ReservationTimeRequestDto(time1Id, time1startAt),
                new ReservationThemeRequestDto(theme1Id, theme1Name, theme1description, theme1Thumbnail));

        // when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all().extract().response();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo("예약자 명 입력해주세요");
    }

    @DisplayName("예약일자가 null이거나 빈 문자열이면 예외가 발생합니다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createReservationEmptyDate(final String date) {
        // given
        final ReservationRequestDto request = new ReservationRequestDto("제이슨", date,
                new ReservationTimeRequestDto(time1Id, time1startAt),
                new ReservationThemeRequestDto(theme1Id, theme1Name, theme1description, theme1Thumbnail));

        // when
        final Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all().extract().response();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("date")).isEqualTo("예약일자를 입력해주세요");
    }

    @DisplayName("특정 일자와 테마 선택 시 기 예약된 시간을 제외한 시간을 반환한다.")
    @Test
    void getAvaliableTimes() {
        // given
        final String date = "2024-12-25";
        final Long themeId = theme1Id;

        final String date2 = "2025-12-31";
        final Long themeId2 = theme1Id;

        // when
        final Response response = RestAssured.given().log().all()
                .queryParam("date", date)
                .queryParam("themeId", themeId)
                .when().get("/times/available")
                .then().log().all().extract().response();

        final Response response2 = RestAssured.given().log().all()
                .queryParam("date", date2)
                .queryParam("themeId", themeId2)
                .when().get("/times/available")
                .then().log().all().extract().response();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", ReservationTimeResponseDto.class)).hasSize(2);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response2.jsonPath().getList(".", ReservationTimeResponseDto.class)).hasSize(3);
    }
}
