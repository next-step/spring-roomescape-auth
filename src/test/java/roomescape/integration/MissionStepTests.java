package roomescape.integration;

import java.util.HashMap;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.DataTimeFormatterUtils;
import roomescape.auth.JwtCookieManager;
import roomescape.auth.JwtTokenProvider;
import roomescape.web.controller.dto.MemberResponse;
import roomescape.domain.MemberRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MissionStepTests {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	private String adminToken;

	private String userToken;

	@BeforeEach
	void setup() {
		MemberResponse admin = new MemberResponse(1L, "admin", "admin@nextstep.com", MemberRole.ADMIN.name());
		MemberResponse user = new MemberResponse(2L, "tester", "tester@gmail.com", MemberRole.USER.name());
		this.adminToken = this.jwtTokenProvider.createToken(admin);
		this.userToken = this.jwtTokenProvider.createToken(user);
	}

	@Test
	void accessPage() {
		Cookie cookie = JwtCookieManager.createCookie(this.adminToken, 3600);
		RestAssured.given()
			.log()
			.all()
			.contentType(ContentType.JSON)
			.cookie(cookie.getName(), cookie.getValue())
			.when()
			.get("/admin/")
			.then()
			.log()
			.all()
			.statusCode(200);
	}

	@Test
	void crdReservation() {
		Cookie cookie = JwtCookieManager.createCookie(this.userToken, 3600);
		createTheme();
		createReservationTime();
		Map<String, String> params = new HashMap<>();
		params.put("name", "브라운");
		params.put("date", DataTimeFormatterUtils.getFormattedTomorrowDate());
		params.put("timeId", "1");
		params.put("themeId", "1");

		RestAssured.given()
			.log()
			.all()
			.contentType(ContentType.JSON)
			.cookie(cookie.getName(), cookie.getValue())
			.body(params)
			.when()
			.post("/reservations")
			.then()
			.log()
			.all()
			.statusCode(201)
			.body("id", is(1));

		RestAssured.given()
			.log()
			.all()
			.when()
			.get("/reservations")
			.then()
			.log()
			.all()
			.statusCode(200)
			.body("size()", is(1));

		RestAssured.given().log().all().when().delete("/reservations/1").then().log().all().statusCode(204);

		RestAssured.given()
			.log()
			.all()
			.when()
			.get("/reservations")
			.then()
			.log()
			.all()
			.statusCode(200)
			.body("size()", is(0));
	}

	@Test
	void crdReservationTime() {
		createReservationTime();

		RestAssured.given().log().all().when().get("/times").then().log().all().statusCode(200).body("size()", is(1));
		RestAssured.given().log().all().when().delete("/times/1").then().log().all().statusCode(204);
	}

	private void createReservationTime() {
		Map<String, String> params = new HashMap<>();
		params.put("startAt", "15:40");

		RestAssured.given()
			.log()
			.all()
			.contentType(ContentType.JSON)
			.body(params)
			.when()
			.post("/times")
			.then()
			.log()
			.all()
			.statusCode(201)
			.body("id", is(1));
	}

	private void createTheme() {
		Map<String, String> params = new HashMap<>();
		params.put("name", "테마1");
		params.put("description", "첫번째테마");
		params.put("thumbnail", "테마이미지");

		RestAssured.given()
			.log()
			.all()
			.contentType(ContentType.JSON)
			.body(params)
			.when()
			.post("/themes")
			.then()
			.log()
			.all()
			.statusCode(201)
			.body("id", is(1));
	}

	@DisplayName("로그인, 로그인 체크, 로그 아웃에 대한 테스트")
	@Test
	void loginIntegration() {
		Map<String, Object> loginParams = new HashMap<>();
		loginParams.put("email", "tester@gmail.com");
		loginParams.put("password", "1234");

		var token = RestAssured.given()
			.log()
			.all()
			.contentType(ContentType.JSON)
			.body(loginParams)
			.when()
			.post("/login")
			.then()
			.log()
			.all()
			.statusCode(200)
			.extract()
			.cookie("token");

		RestAssured.given()
			.log()
			.all()
			.cookie("token", token)
			.when()
			.get("/login/check")
			.then()
			.log()
			.all()
			.statusCode(200)
			.body("role", is("USER"));

		RestAssured.given().log().all().cookie("token", token).post("/logout").then().log().all().statusCode(200);

		RestAssured.given().log().all().when().get("/login/check").then().log().all().statusCode(401);
	}

	@Test
	void createMember() {
		Map<String, String> params = new HashMap<>();
		params.put("name", "이름");
		params.put("email", "name@gmail.com");
		params.put("password", "1234");

		RestAssured.given()
			.log()
			.all()
			.contentType(ContentType.JSON)
			.body(params)
			.when()
			.post("/members")
			.then()
			.log()
			.all()
			.statusCode(201)
			.body("name", is("이름"))
			.body("email", is("name@gmail.com"));
	}

}
