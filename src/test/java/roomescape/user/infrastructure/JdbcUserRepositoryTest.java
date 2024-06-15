package roomescape.user.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import roomescape.user.domain.User;
import roomescape.user.domain.repository.UserRepository;

@JdbcTest
class JdbcUserRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new JdbcUserRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("이메일로 회원을 조회한다.")
    void findByEmail() {
        // given
        jdbcTemplate.execute("INSERT INTO users(email, password) VALUES ('email@email.com', '1234')");

        String email = "email@email.com";

        // when
        User user = userRepository.findByEmail(email).get();

        // then
        assertThat(user.getEmail()).isEqualTo(email);
    }
}
