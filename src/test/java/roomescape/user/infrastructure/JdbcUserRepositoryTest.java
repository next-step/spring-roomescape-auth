package roomescape.user.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
    @DisplayName("아이디로 회원을 조회한다.")
    void findById() {
        // given
        Long id = 1L;

        // when
        User user = userRepository.findById(id).get();

        // then
        assertThat(user.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("이메일로 회원을 조회한다.")
    void findByEmail() {
        // given
        String email = "admin@email.com";

        // when
        User user = userRepository.findByEmail(email).get();

        // then
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    void 가입된_모든_회원을_조회한다() {
        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(2);
    }
}
