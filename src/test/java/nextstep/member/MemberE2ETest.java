package nextstep.member;

import io.restassured.RestAssured;
import nextstep.auth.presentation.dto.TokenRequest;
import nextstep.auth.presentation.dto.TokenResponse;
import nextstep.member.presentation.dto.CreateMemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MemberE2ETest {

    @DisplayName("멤버를 생성한다")
    @Test
    public void create() {
        CreateMemberRequest body = new CreateMemberRequest("username", "password", "name", "010-1234-5678");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("토큰 없이 나를 조회한다")
    @Test
    public void me_noToken() {
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/members/me")
            .then().log().all()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("토큰으로 나를 조회한다")
    @Test
    public void me_withToken() {
        CreateMemberRequest memberRequest = new CreateMemberRequest("username", "password", "name", "010-1234-5678");
        회원가입(memberRequest);
        TokenResponse tokenResponse = 로그인(new TokenRequest(memberRequest.getUsername(), memberRequest.getPassword()));

        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("authorization", "Bearer " + tokenResponse.getAccessToken())
            .when().get("/members/me")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    private TokenResponse 로그인(TokenRequest body) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/login/token")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(TokenResponse.class);
    }

    void 회원가입(CreateMemberRequest body) {
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/members");
    }
}
