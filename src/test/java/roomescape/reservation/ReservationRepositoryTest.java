package roomescape.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.infra.ReservationRepository;
import roomescape.reservationTheme.domain.ReservationTheme;
import roomescape.reservationTheme.infra.ReservationThemeRepository;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.infra.ReservationTimeRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
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

        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation");
        jdbcTemplate.execute("DROP TABLE IF EXISTS theme");
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation_time");

        jdbcTemplate.execute(
                """
                        CREATE TABLE reservation_time (
                            id BIGINT NOT NULL AUTO_INCREMENT, 
                            start_at VARCHAR(255) NOT NULL,
                            PRIMARY KEY (id)
                        )
                        """
        );


        jdbcTemplate.execute("""
                CREATE TABLE theme (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            name VARCHAR(255) NOT NULL,
                            description VARCHAR(255) NOT NULL,
                            thumbnail VARCHAR(255) NOT NULL,
                            PRIMARY KEY (id))
                """);

        jdbcTemplate.execute(
                """
                        CREATE TABLE reservation (
                            id BIGINT NOT NULL AUTO_INCREMENT, 
                            name VARCHAR(255) NOT NULL, 
                            date VARCHAR(255) NOT NULL, 
                            time_id BIGINT,
                            theme_id BIGINT,
                            PRIMARY KEY (id),
                            FOREIGN KEY (time_id) REFERENCES reservation_time (id),
                            FOREIGN KEY (theme_id) REFERENCES theme (id)
                        )
                        """
        );
    }

    @DisplayName("전체 예약을 조회합니다.")
    @Test
    void findAll() {
        // given
        final ReservationTime requestTime = new ReservationTime("15:40");
        final Long id = reservationTimeRepository.save(requestTime);
        final ReservationTime savedTime = reservationTimeRepository.findById(id);

        final ReservationTheme reservationTheme1 = new ReservationTheme("테마1", "설명1", "썸네일1");
        final ReservationTheme reservationTheme2 = new ReservationTheme("테마2", "설명2", "썸네일2");
        final Long savedThemeId1 = reservationThemeRepository.save(reservationTheme1);
        final Long savedThemeId2 = reservationThemeRepository.save(reservationTheme2);
        final ReservationTheme savedTheme1 = reservationThemeRepository.findById(savedThemeId1);
        final ReservationTheme savedTheme2 = reservationThemeRepository.findById(savedThemeId2);

        final Reservation reservation1 = new Reservation("제이슨", "2024-08-05", savedTime, savedTheme1);
        final Reservation reservation2 = new Reservation("심슨", "2024-08-05", savedTime, savedTheme2);

        final List<Object[]> reservations = Arrays.asList(reservation1, reservation2).stream()
                .map(reservation -> new Object[]{
                        reservation.getName(), reservation.getDate(),
                        reservation.getReservationTime().getId(),
                        reservation.getReservationTheme().getId()
                })
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate("INSERT INTO reservation(name, date, time_id, theme_id) VALUES (?,?,?,?)", reservations);

        // when
        final List<Reservation> actual = reservationRepository.findAll();

        // then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("예약을 저장합니다.")
    @Test
    void save() {
        // given
        final ReservationTime requestTime = new ReservationTime("15:40");
        final Long id = reservationTimeRepository.save(requestTime);
        final ReservationTime savedTime = reservationTimeRepository.findById(id);

        final ReservationTheme reservationTheme = new ReservationTheme("테마3", "설명3", "썸네일3");
        final Long savedThemeId = reservationThemeRepository.save(reservationTheme);
        final ReservationTheme savedTheme = reservationThemeRepository.findById(savedThemeId);

        final Reservation request = new Reservation("테스트", "2024-08-05", savedTime, savedTheme);

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
        final ReservationTime requestTime = new ReservationTime("15:40");
        final Long id = reservationTimeRepository.save(requestTime);
        final ReservationTime savedTime = reservationTimeRepository.findById(id);

        final ReservationTheme reservationTheme = new ReservationTheme("테마3", "설명3", "썸네일3");
        final Long savedThemeId = reservationThemeRepository.save(reservationTheme);
        final ReservationTheme savedTheme = reservationThemeRepository.findById(savedThemeId);

        final Reservation request = new Reservation("테스트", "2024-08-05", savedTime, savedTheme);
        final Long savedReservationId = reservationRepository.save(request);

        // when
        reservationRepository.deleteById(savedReservationId);

        // then
        assertThatThrownBy(() -> reservationRepository.findById(savedReservationId))
                .isInstanceOf(DataAccessException.class);
    }
}
