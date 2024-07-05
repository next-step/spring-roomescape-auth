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
public class MemberReadTest {
    @Autowired
    private SignUpService signUpService;

    @BeforeEach
    public void setPort() {
        RestAssured.port = 8888;
    }

    @Test
    @DisplayName("전체 회원 조회")
    void readAllMembers() {
        signUpService.signUp(new MemberRequest("yeeun", "anna862700@gmail.com", "password"), "GUEST");
        signUpService.signUp(new MemberRequest("asdf", "asdf@gmail.com", "password"), "GUEST");

        var response = RestAssured
                .given().log().all()
                .when().get("/members")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.jsonPath().getList("", MemberResponse.class)).hasSize(2);
    }

    @Test
    @DisplayName("회원 하나도 없는 경우 전체 회원 조회")
    void readAllMembersIfNoMembers() {
        var response = RestAssured
                .given().log().all()
                .when().get("/members")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.jsonPath().getList("", ReservationResponse.class)).hasSize(0);
    }

    @Test
    @DisplayName("id로 특정 회원 조회")
    void readMemberIfMemberMatchWithIdExist() {
        String name = "yeeun";
        String email = "anna862700@gmail.com";
        signUpService.signUp(new MemberRequest(name, email, "password"), "GUEST");

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
    @DisplayName("예외 - 존재하지 않는 id로 회원 조회")
    void failToReadMemberIfMemberMatchWithIdNotExist() {
        String name = "yeeun";
        String email = "anna862700@gmail.com";
        signUpService.signUp(new MemberRequest(name, email, "password"), "GUEST");

        RestAssured.given().log().all()
                .when().get("/members/2")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
