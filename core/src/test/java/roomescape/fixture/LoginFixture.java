package roomescape.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import roomescape.auth.application.dto.LoginRequest;
import roomescape.domain.user.domain.User;
import roomescape.domain.user.domain.UserRepository;
import roomescape.domain.user.domain.UserRole;
import roomescape.support.RestAssuredTestSupport;

@Component
public class LoginFixture extends RestAssuredTestSupport {

    @Autowired
    UserRepository userRepository;

    /**
     * 로그인 요청
     *
     * @param userName 유저 이름
     * @return accessToken
     */
    public String loginWithUserName(String userName) {
        User user = createUser(userName);
        return login(user);
    }

    /**
     * 로그인 요청
     *
     * @return accessToken
     */
    public String login(User user) {
        final LoginRequest loginRequest = new LoginRequest(user.getEmail(), user.getPassword());

        final ValidatableResponse loginResponse = RestAssured
                .given().log().all()
                .body(loginRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/login")
                .then().log().all()
                .statusCode(200); // 로그인 성공 검증

        return loginResponse.extract().cookie("accessToken");
    }

    private User createUser(final String userName) {
        User user = User.builder()
                .role(UserRole.CUSTOMER)
                .email("email")
                .password("password")
                .name(userName)
                .build();
        return userRepository.save(user);
    }
}
