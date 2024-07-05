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
public class ThemeCreateTest {
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
    @DisplayName("테마 생성")
    void createTheme() {
        String token = createToken();
        String name = "수키도키";
        String description = "흐르는 대로 살자 해파리처럼🪼";
        String thumbnail = "https://pbs.twimg.com/media/GApx6fjagAAkFsX.jpg";

        var body = RestAssured
                .given().log().all()
                .cookie("token", token)
                .body(ThemeRequest.of(name, description, thumbnail))
                .contentType(ContentType.JSON)
                .when().post("/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(ThemeResponse.class);

        assertThat(body.name()).isEqualTo(name);
        assertThat(body.description()).isEqualTo(description);
        assertThat(body.thumbnail()).isEqualTo(thumbnail);
    }

    @Test
    @DisplayName("예외 - 이미 존재하는 이름의 테마 생성")
    void failToCreateIfThemeNameAlreadyExist() {
        String token = createToken();
        String name = "수키도키";
        String description = "흐르는 대로 살자 해파리처럼🪼";
        String thumbnail = "https://pbs.twimg.com/media/GApx6fjagAAkFsX.jpg";
        themeService.add(ThemeRequest.of(name, description, thumbnail));

        RestAssured
                .given().log().all()
                .cookie("token", token)
                .body(ThemeRequest.of(name, description, thumbnail))
                .contentType(ContentType.JSON)
                .when().post("/themes")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract();
    }
}

