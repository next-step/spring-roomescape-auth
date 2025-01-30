package roomescape.domain.user.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.support.IntegrationTestSupport;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserRepositoryTest extends IntegrationTestSupport {

    @Autowired
    UserRepository sut;

    @Test
    void save() {
        // given
        final User user = User.builder()
                .role(UserRole.CUSTOMER)
                .name("name")
                .email("email")
                .password("password")
                .build();

        // when
        final User actual = sut.save(user);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getRole()).isEqualTo(UserRole.CUSTOMER),
                () -> assertThat(actual.getName()).isEqualTo("name"),
                () -> assertThat(actual.getEmail()).isEqualTo("email"),
                () -> assertThat(actual.getPassword()).isEqualTo("password")
        );
    }

    @Test
    void save_already_has_id() {
        // given
        final User user = User.builder()
                .role(UserRole.CUSTOMER)
                .name("name")
                .email("email")
                .password("password")
                .build();
        final User saved = sut.save(user);

        final User toBeUpdated = User.builder()
                .id(saved.getId())
                .role(UserRole.ADMIN)
                .name("name_update")
                .email("email_update")
                .password("password_update")
                .build();

        // when
        final User actual = sut.save(toBeUpdated);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(saved.getId()),
                () -> assertThat(actual.getRole()).isEqualTo(UserRole.ADMIN),
                () -> assertThat(actual.getName()).isEqualTo("name_update"),
                () -> assertThat(actual.getEmail()).isEqualTo("email_update"),
                () -> assertThat(actual.getPassword()).isEqualTo("password_update")
        );
    }

    @Test
    void findByEmail() {
        // given
        final User user = User.builder()
                .role(UserRole.CUSTOMER)
                .name("name")
                .email("email")
                .password("password")
                .build();
        final User saved = sut.save(user);

        // when
        Optional<User> actualOpt = sut.findByEmail("email");

        // then
        final User actual = actualOpt.get();
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(saved.getId()),
                () -> assertThat(actual.getRole()).isEqualTo(UserRole.CUSTOMER),
                () -> assertThat(actual.getName()).isEqualTo("name"),
                () -> assertThat(actual.getEmail()).isEqualTo("email"),
                () -> assertThat(actual.getPassword()).isEqualTo("password")
        );
    }
}
