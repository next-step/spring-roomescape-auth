package roomescape.member;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.member.application.SignUpService;
import roomescape.member.ui.dto.MemberRequest;
import roomescape.member.ui.dto.MemberResponse;
import roomescape.reservation.ui.dto.ReservationResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8888"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberTest {
    @Autowired
    private SignUpService signUpService;

    @BeforeEach
    public void setPort() {
        RestAssured.port = 8888;
    }

    @Test
    @DisplayName("전체 사용자 조회")
    void 전체_사용자_조회() {
        signUpService.signUp(new MemberRequest("yeeun", "anna862700@gmail.com", "password"));
        signUpService.signUp(new MemberRequest("asdf", "asdf@gmail.com", "password"));

        var response = RestAssured
                .given().log().all()
                .when().get("/members")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.jsonPath().getList("", MemberResponse.class)).hasSize(2);
    }

    @Test
    @DisplayName("사용자 하나도 없는 경우 전체 사용자 조회")
    void 사용자_없는_경우_전체_사용자_조회() {
        var response = RestAssured
                .given().log().all()
                .when().get("/members")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.jsonPath().getList("", ReservationResponse.class)).hasSize(0);
    }

    @Test
    @DisplayName("id로 특정 사용자 조회")
    void 단일_사용자_조회() {
        String name = "yeeun";
        String email = "anna862700@gmail.com";
        signUpService.signUp(new MemberRequest(name, email, "password"));

        var response = RestAssured
                .given().log().all()
                .when().get("/members/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(MemberResponse.class);

        assertThat(response.name()).isEqualTo(name);
        assertThat(response.email()).isEqualTo(email);
    }

    @Test
    @DisplayName("예외 - 존재하지 않는 id로 사용자 조회")
    void 존재하지_않는_단일_사용자_조회() {
        String name = "yeeun";
        String email = "anna862700@gmail.com";
        signUpService.signUp(new MemberRequest(name, email, "password"));

        RestAssured.given().log().all()
                .when().get("/members/2")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
