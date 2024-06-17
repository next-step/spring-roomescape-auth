package roomescape.util;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("쿠키 관련 테스트")
class CookieUtilsTest {

    @DisplayName("응답 쿠키를 생성한다.")
    @Test
    void createResponseCookie() {
        final String name = "name";
        final String value = "test";
        final int maxAge = 60;

        ResponseCookie responseCookie = CookieUtils.createResponseCookie(name, value, maxAge);

        Assertions.assertAll(
                () -> assertThat(responseCookie.getName()).isEqualTo(name),
                () -> assertThat(responseCookie.getValue()).isEqualTo(value),
                () -> assertThat(responseCookie.getMaxAge().getSeconds()).isEqualTo(maxAge)
        );
    }

    @DisplayName("쿠키 이름에 해당하는 쿠키 값을 쿠키 목록에서 추출한다.")
    @Test
    void extractCookieValue() {
        Cookie[] cookies = new Cookie[]{
                new Cookie("name1", "value1"),
                new Cookie("name2", "value2"),
                new Cookie("name3", "value3"),
        };

        String cookieValue = CookieUtils.extractCookieValue(cookies, "name1");

        assertThat(cookieValue).isEqualTo("value1");
    }

    @DisplayName("쿠키 이름에 해당하는 쿠키 값을 쿠키 목록에서 만료시킨다.")
    @Test
    void removeCookieByName() {
        Cookie cookie1 = new Cookie("name1", "value1");
        cookie1.setMaxAge(60);

        Cookie cookie2 = new Cookie("name2", "value2");
        cookie2.setMaxAge(60);

        Cookie cookie = CookieUtils.expireCookieByName(new Cookie[]{cookie1, cookie2}, "name1");

        assertThat(cookie.getMaxAge()).isEqualTo(0);
    }
}
