package roomescape.reservationtime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import roomescape.globalfixture.entity.ReservationTimeFixture;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.infra.ReservationTimeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@TestPropertySource(locations = "classpath:test-application.yml")
@Sql(scripts = "/test-schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationTimeRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = new ReservationTimeRepository(jdbcTemplate);
    }

    @DisplayName("예약 시간을 등록할 수 있습니다.")
    @Test
    void creatTime() {
        // given
        final ReservationTime request = ReservationTimeFixture.createReservationTime();
        // when
        final Long id = reservationTimeRepository.save(request);

        // then
        final ReservationTime actual = reservationTimeRepository.findById(id);
        assertThat(actual.getStartAt()).isEqualTo(request.getStartAt());
    }

    @DisplayName("예약 시간을 삭제할 수 있습니다.")
    @Test
    void deleteTime() {
        // given
        final ReservationTime request = ReservationTimeFixture.createReservationTime();
        final Long id = reservationTimeRepository.save(request);

        // when
        reservationTimeRepository.deleteById(id);

        // then
        assertThat(reservationTimeRepository.existsById(id)).isFalse();
    }

    @DisplayName("예약 시간을 조회할 수 있습니다.")
    @Test
    void findAll() {
        // when
        final List<ReservationTime> actual = reservationTimeRepository.findAll();

        // then
        assertThat(actual).hasSize(3);
    }

    @DisplayName("예약 시간을 조회할 수 있습니다.")
    @Test
    void findById() {
        // given
        final ReservationTime request = ReservationTimeFixture.createReservationTime();
        final Long id = reservationTimeRepository.save(request);

        // when
        final ReservationTime actual = reservationTimeRepository.findById(id);

        // then
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getStartAt()).isEqualTo(request.getStartAt());
    }
}
