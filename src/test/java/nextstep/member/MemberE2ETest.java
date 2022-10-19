package nextstep.member;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.TokenRequest;
import nextstep.auth.TokenResponse;
import nextstep.support.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MemberE2ETest {
    @DisplayName("멤버를 생성한다")
    @Test
    void create() {
        // given
        MemberRequest request = new MemberRequest("user1", "password", "name", "010-1234-5678", "ADMIN");

        // when
        ExtractableResponse<Response> response = createMember(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("중복 username 존재 시, 예외를 반환한다")
    @Test
    void failToCreate() {
        // given
        MemberRequest request = new MemberRequest("user2", "password", "name", "010-1234-5678", "ADMIN");
        createMember(request);

        // when
        ExtractableResponse<Response> response = createMember(request);
        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.getMessage()).isEqualTo("입력하신 username은 이미 사용중입니다.");

    }

    @DisplayName("내 정보를 조회한다")
    @Test
    void showMe() {
        // given
        MemberRequest request = new MemberRequest("user3", "password", "name", "010-1234-5678", "ADMIN");
        createMember(request);
        String token = login().as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> response = findMember(token);
        Member member = response.as(Member.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(member.getUsername()).isNotNull();
    }

    @DisplayName("로그인하지 않고 내 정보 조회를 호출하면, 예외를 반환한다")
    @Test
    void failToFindMe() {
        // given, when
        ExtractableResponse<Response> response = findMember("");
        ErrorResponse errorResponse = response.as(ErrorResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(errorResponse.getMessage()).isEqualTo("로그인 후 이용가능합니다.");
    }

    private ExtractableResponse<Response> createMember(MemberRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> findMember(String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().get("/members/me")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> login() {
        TokenRequest request = new TokenRequest("user3", "password");

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }
}
