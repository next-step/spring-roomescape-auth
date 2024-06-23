package roomescape.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import roomescape.GloblaFixture.entity.ReservationFixture;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.infra.ReservationRepository;
import roomescape.reservationTheme.infra.ReservationThemeRepository;
import roomescape.reservationTime.infra.ReservationTimeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@TestPropertySource(locations = "classpath:test-application.yml")
@Sql(scripts = "/test-schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationRepositoryTest {

    private ReservationRepository reservationRepository;
    private ReservationThemeRepository reservationThemeRepository;
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        reservationRepository = new ReservationRepository(jdbcTemplate);
        reservationTimeRepository = new ReservationTimeRepository(jdbcTemplate);
        reservationThemeRepository = new ReservationThemeRepository(jdbcTemplate);
    }

    @DisplayName("전체 예약을 조회합니다.")
    @Test
    void findAll() {
        // when
        final List<Reservation> actual = reservationRepository.findAll();

        // then
        assertThat(actual).hasSize(1);
    }

    @DisplayName("예약을 저장합니다.")
    @Test
    void save() {
        // given
        final Reservation request = ReservationFixture.createReservation();

        // when
        final Long savedReservationId = reservationRepository.save(request);

        // then
        final Reservation actual = reservationRepository.findById(savedReservationId);
        assertThat(actual.getDate()).isEqualTo(request.getDate());
        assertThat(actual.getName()).isEqualTo(request.getName());
    }

    @DisplayName("예약 삭제을 삭제합니다.")
    @Test
    void deleteById() {
        // given
        final Reservation request = ReservationFixture.createReservation();
        final Long savedReservationId = reservationRepository.save(request);

        // when
        reservationRepository.deleteById(savedReservationId);

        // then
        assertThatThrownBy(() -> reservationRepository.findById(savedReservationId))
                .isInstanceOf(DataAccessException.class);
    }
}
