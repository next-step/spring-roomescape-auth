package nextstep.theme;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class ThemeDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void save() {
        // given
        ThemeDao themeDao = new ThemeDao(jdbcTemplate);

        // when
        Long id = themeDao.save(new Theme("테마 이름", "테마 설명", 22_000));

        // then
        assertThat(id).isNotNull();
    }
}
