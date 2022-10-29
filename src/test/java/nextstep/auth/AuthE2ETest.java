package nextstep.auth;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static nextstep.utils.RequestFixture.memberRequest;
import static nextstep.utils.RequestFixture.tokenRequest;
import static nextstep.utils.Requests.createMemberRequest;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthE2ETest {

    @DisplayName("토큰을 생성한다")
    @Test
    public void create() {
        var memberRequest = memberRequest();
        createMemberRequest(memberRequest);

        var response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequest(memberRequest))
            .when().post("/login/token")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
        assertThat(response.as(TokenResponse.class)).isNotNull();
    }
}
