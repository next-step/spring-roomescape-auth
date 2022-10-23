package nextstep.member;

import io.restassured.RestAssured;
import nextstep.auth.presentation.dto.TokenRequest;
import nextstep.auth.presentation.dto.TokenResponse;
import nextstep.member.presentation.dto.CreateMemberRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static nextstep.DataLoader.ADMIN_PASSWORD;
import static nextstep.DataLoader.ADMIN_USERNAME;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AdminE2ETest {

    @Autowired
    private MemberService memberService;

    @Test
    void getAdmin_withToken() {
        TokenResponse tokenResponse = 로그인(new TokenRequest(ADMIN_USERNAME, ADMIN_PASSWORD));

        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("authorization", "Bearer " + tokenResponse.getAccessToken())
            .when().get("/admin")
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void getAdmin_noToken() {
        CreateMemberRequest memberRequest = new CreateMemberRequest("username", "password", "name", "010-1234-5678");
        memberService.createAdmin(memberRequest);

        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/admin")
            .then().log().all()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
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
}
