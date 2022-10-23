package nextstep.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static nextstep.auth.AuthE2ETest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MemberE2ETest {
    @DisplayName("멤버를 생성한다")
    @Test
    void create() {
        MemberRequest body = new MemberRequest("username", "password", "name", "010-1234-5678");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("로그인된 회원의 정보를 조회한다")
    @Test
    void me() {
        //given
        String userName = "Gomding";
        String password = "q1w2e3r4";

        회원가입_한다("Gomding123", "q1w2e3r4123");
        회원가입_한다(userName, password);
        String token = 로그인_한다(userName, password);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getUsername()).isEqualTo(userName);
        assertThat(memberResponse.getPassword()).isEqualTo(password);
    }

    public static Long 회원가입_한다(String userName, String password) {
        MemberRequest body = new MemberRequest(userName, password, "name", "010-1234-5678");
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/members")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return Long.parseLong(response.header("Location").split("^*/")[2]);
    }
}
