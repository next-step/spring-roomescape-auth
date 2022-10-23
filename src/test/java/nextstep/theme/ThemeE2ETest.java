package nextstep.theme;

import io.restassured.RestAssured;
import nextstep.auth.TokenRequest;
import nextstep.auth.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ThemeE2ETest {

    @DisplayName("테마 목록을 조회한다")
    @Test
    public void showThemes() {
        createTheme();

        var response = RestAssured
            .given().log().all()
            .param("date", "2022-08-11")
            .when().get("/themes")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
        assertThat(response.jsonPath().getList(".").size()).isEqualTo(1);
    }

    private void createTheme() {
        String token = adminLogin();
        ThemeRequest body = new ThemeRequest("테마이름", "테마설명", 22000);

        RestAssured
            .given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/admin/themes")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract().header("Location");
    }

    private String adminLogin() {
        TokenRequest body = new TokenRequest("admin", "admin_password");

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
}
