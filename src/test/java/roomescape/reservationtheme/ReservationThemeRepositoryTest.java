package roomescape.reservationtheme;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import roomescape.globalfixture.entity.ReservationThemeFixture;
import roomescape.reservationtheme.domain.ReservationTheme;
import roomescape.reservationtheme.infra.ReservationThemeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@TestPropertySource(locations = "classpath:test-application.yml")
@Sql(scripts = "/test-schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationThemeRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private ReservationThemeRepository reservationThemeRepository;

    @BeforeEach
    void setUp(){
        reservationThemeRepository = new ReservationThemeRepository(jdbcTemplate);
    }

    @DisplayName("테마를 저장할 수 있습니다.")
    @Test
    void save() {
        //given
        final ReservationTheme request = ReservationThemeFixture.createReservationTheme();
        final Long id = reservationThemeRepository.save(request);

        //when
        final ReservationTheme savedTheme = reservationThemeRepository.findById(id);

        //then
        assertThat(savedTheme.getName()).isEqualTo(request.getName());
    }

    @DisplayName("모든 테마를 조회할 수 있습니다.")
    @Test
    void findAll() {
        //when
        final List<ReservationTheme> savedThemes = reservationThemeRepository.findAll();

        //then
        assertThat(savedThemes.size()).isEqualTo(2);
    }

    @DisplayName("테마를 조회할 수 있습니다.")
    @Test
    void findById() {
        //given
        final ReservationTheme request = ReservationThemeFixture.createReservationTheme();
        final Long requestId = reservationThemeRepository.save(request);

        //when
        final ReservationTheme savedTheme = reservationThemeRepository.findById(requestId);

        //then
        assertThat(savedTheme.getName()).isEqualTo(request.getName());
    }

    @DisplayName("테마를 삭제할 수 있습니다.")
    @Test
    void deleteById() {
        //given
        final ReservationTheme request = ReservationThemeFixture.createReservationTheme();
        final Long id = reservationThemeRepository.save(request);

        //when
        reservationThemeRepository.deleteById(id);

        //then
        assertThat(reservationThemeRepository.existById(id)).isFalse();
    }

    @DisplayName("테마가 존재하는지 확인할 수 있습니다.")
    @Test
    void existById() {
        //given
        final ReservationTheme request = ReservationThemeFixture.createReservationTheme();
        final Long id = reservationThemeRepository.save(request);

        //when
        final boolean isExisted = reservationThemeRepository.existById(id);

        //then
        assertThat(isExisted).isTrue();
    }
}
