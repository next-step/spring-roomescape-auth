package roomescape.theme.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.repository.ThemeRepository;

@JdbcTest
class JdbcThemeRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ThemeRepository themeRepository;

    private Theme theme;
    private Theme savedTheme;

    @BeforeEach
    void setUp() {
        themeRepository = new JdbcThemeRepository(jdbcTemplate, dataSource);

        theme = new Theme("레벨2 탈출","우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        savedTheme = themeRepository.save(theme);
    }

    @Test
    @DisplayName("테마를 저장한다.")
    void save() {
        Theme savedTheme = themeRepository.save(theme);

        assertThat(savedTheme.getId()).isNotNull();
        assertThat(savedTheme).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(theme);
    }

    @Test
    @DisplayName("테마를 조회한다.")
    void findById() {
        Theme findTheme = themeRepository.findById(savedTheme.getId()).get();

        assertThat(findTheme).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(theme);
    }

    @Test
    @DisplayName("모든 테마를 조회한다.")
    void findAll() {
        List<Theme> themes = themeRepository.findAll();

        assertThat(themes).hasSize(4);
    }

    @Test
    @DisplayName("테마가 존재하면 TRUE를 반환한다.")
    void existsById_ReturnTrue() {
        boolean result = themeRepository.existsById(savedTheme.getId());

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("테마가 존재하면 FALSE를 반환한다.")
    void existsById_ReturnFalse() {
        boolean result = themeRepository.existsById(10L);

        assertThat(result).isFalse();
    }
}
