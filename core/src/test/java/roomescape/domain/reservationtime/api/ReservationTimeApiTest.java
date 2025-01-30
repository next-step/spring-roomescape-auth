package roomescape.domain.reservationtime.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.domain.common.SystemClockHolder;
import roomescape.domain.reservation.domain.*;
import roomescape.domain.reservationtime.application.response.AvailableReservationTimeResponse;
import roomescape.domain.reservationtime.domain.ReservationTime;
import roomescape.domain.reservationtime.domain.ReservationTimeRepository;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.theme.domain.ThemeRepository;
import roomescape.domain.user.domain.User;
import roomescape.domain.user.domain.UserRepository;
import roomescape.domain.user.domain.UserRole;
import roomescape.fixture.LoginFixture;
import roomescape.support.RestAssuredTestSupport;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.hamcrest.Matchers.is;

class ReservationTimeApiTest extends RestAssuredTestSupport {

    @Autowired
    ReservationTimeRepository timeRepository;

    @Autowired
    ThemeRepository themeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    SystemClockHolder clockHolder;

    @Autowired
    LoginFixture loginFixture;

    @DisplayName("특정 날짜와 테마에 대해 예약 가능 시간대를 조회한다.")
    @Test
    void getAvailableReservationTimes() {
        // given
        ReservationTime time10 = createTime(LocalTime.of(10, 0));
        ReservationTime time12 = createTime(LocalTime.of(12, 0));
        ReservationTime time14 = createTime(LocalTime.of(14, 0));

        Theme theme = createTheme("테마A");
        User user = createUser("홍길동");

        createReservation(theme, LocalDate.of(2025, 1, 27), time10, user); // 2025-01-27 10:00 예약

        String accessToken = loginFixture.login(user);

        LocalDate date = LocalDate.of(2025, 1, 27);
        Long themeId = theme.getId().value();

        // when
        ValidatableResponse response = RestAssured
                .given().log().all()
                .cookie("accessToken", accessToken)
                .queryParam("date", date.toString())
                .queryParam("themeId", themeId)
                .when().get("/api/times/available")
                .then().log().all();

        // then
        response.statusCode(200);

        List<AvailableReservationTimeResponse> responseBody = response.extract()
                .jsonPath()
                .getList("data", AvailableReservationTimeResponse.class);

        assertThat(responseBody).hasSize(2)
                .extracting("timeId", "startAt")
                .containsExactlyInAnyOrder(
                        tuple(time12.getId().value(), LocalTime.of(12, 0)),
                        tuple(time14.getId().value(), LocalTime.of(14, 0))
                );
    }

    @DisplayName("예약 가능한 시간대 조회 API 호출 시 인증 정보가 없으면 401 리턴")
    @Test
    void getAvailableReservationTimes_no_authentication() {
        // given
        LocalDate date = LocalDate.of(2025, 1, 27);
        Long themeId = -1000L;

        // when
        ValidatableResponse response = RestAssured
                .given().log().all()
                // no accessToken
                .queryParam("date", date.toString())
                .queryParam("themeId", themeId)
                .when().get("/api/times/available")
                .then().log().all();

        // then
        response.statusCode(401);
    }

    @Test
    void reservationTime() {
        Map<String, String> timeAppendHttpRequest = new HashMap<>();
        timeAppendHttpRequest.put("startAt", "10:00");

        String accessToken = loginFixture.loginWithUserName("홍길동");

        // 예약 시간 추가
        final ValidatableResponse response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)
                .body(timeAppendHttpRequest)
                .when().post("/api/times")
                .then().log().all()
                .statusCode(200)
                .body("data.startAt", is("10:00"));

        final Long timeId = ((Integer) response.extract().path("data.id")).longValue();

        // 예약 시간 전체 조회
        RestAssured.given().log().all()
                .cookie("accessToken", accessToken)
                .when().get("/api/times")
                .then().log().all()
                .statusCode(200)
                .body("data.size()", is(1));

        // 예약 시간 삭제
        RestAssured.given().log().all()
                .cookie("accessToken", accessToken)
                .when()
                .pathParam("timeId", timeId)
                .delete("/api/times/{timeId}")
                .then().log().all()
                .statusCode(200);

        // 예약 시간 삭제 후 조회
        RestAssured.given().log().all()
                .cookie("accessToken", accessToken)
                .when().get("/api/times")
                .then().log().all()
                .statusCode(200)
                .body("data.size()", is(0));
    }

    private ReservationTime createTime(final LocalTime startAt) {
        return timeRepository.save(ReservationTime.builder()
                .startAt(startAt)
                .createdAt(clockHolder.getCurrentSeoulTime())
                .build());
    }

    private Reservation createReservation(final Theme theme, final LocalDate date, final ReservationTime time, final User user) {
        Reservation reservation1 = Reservation.builder()
                .themeId(theme.getId())
                .name(new ReservationGuestName(user.getName()))
                .date(new ReservationDate(date))
                .timeId(time.getId())
                .status(ReservationStatus.CONFIRMED)
                .reservedAt(clockHolder.getCurrentSeoulTime())
                .build();
        return reservationRepository.save(reservation1);
    }

    private User createUser(final String userName) {
        User user = User.builder()
                .role(UserRole.CUSTOMER)
                .email("email")
                .password("password")
                .name(userName)
                .build();
        return userRepository.save(user);
    }

    private Theme createTheme(final String name) {
        Theme theme = Theme.builder()
                .name(name)
                .thumbnail("theme-thumbnail")
                .description("theme-description")
                .build();
        return themeRepository.save(theme);
    }
}
