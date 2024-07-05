package roomescape.theme;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.ui.dto.LoginRequest;
import roomescape.member.ui.dto.MemberRequest;
import roomescape.member.ui.dto.MemberResponse;
import roomescape.theme.application.ThemeService;
import roomescape.theme.ui.dto.ThemeRequest;
import roomescape.theme.ui.dto.ThemeResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeReadTest {
    @Autowired
    private ThemeService themeService;

    @BeforeEach
    public void setPort() {
        RestAssured.port = 8888;
    }

    private String createToken() {
        String name = "yeeun";
        String email = "anna862700@gmail.com";
        String password = "password";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new MemberRequest(name, email, password))
                .when().post("/members")
                .then().extract().body().as(MemberResponse.class);
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(email, password))
                .when().post("/login")
                .then().extract().cookie("token");
    }

    @Test
    @DisplayName("전체 테마 조회")
    void readAllThemes() {
        String token = createToken();
        String name = "수키도키";
        String description = "흐르는 대로 살자 해파리처럼🪼";
        String thumbnail = "https://pbs.twimg.com/media/GApx6fjagAAkFsX.jpg";
        themeService.add(ThemeRequest.of(name, description, thumbnail));

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/themes")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.jsonPath().getList("", ThemeResponse.class)).hasSize(1);
    }

    @Test
    @DisplayName("테마가 하나도 없는 경우 전체 테마 조회")
    void readAllThemesIfNoThemes() {
        String token = createToken();

        var response = RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/themes")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.jsonPath().getList("", ThemeResponse.class)).hasSize(0);
    }

    @Test
    @DisplayName("테마 하나 조회")
    void readTheme() {
        String token = createToken();
        String name = "수키도키";
        String description = "흐르는 대로 살자 해파리처럼🪼";
        String thumbnail = "https://pbs.twimg.com/media/GApx6fjagAAkFsX.jpg";
        themeService.add(ThemeRequest.of(name, description, thumbnail));

        var reservationTime = RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/themes/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(ThemeResponse.class);

        assertThat(reservationTime.name()).isEqualTo(name);
        assertThat(reservationTime.description()).isEqualTo(description);
        assertThat(reservationTime.thumbnail()).isEqualTo(thumbnail);
    }

    @Test
    @DisplayName("예외 - 존재하지 않는 id로 테마 하나 조회")
    void failToReadNonExistentTheme() {
        String token = createToken();

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .when().get("/themes/1")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
