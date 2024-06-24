package roomescape.reservation.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;
import roomescape.user.domain.User;

@JdbcTest
class JdbcReservationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new JdbcReservationRepository(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("예약을 데이터베이스에 저장한다.")
    void testSaveReservation() {
        // given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.parse("15:40"));
        Theme theme = new Theme(1L, "레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        User user = new User(2L, "브라운", "brown@email.com", "password", "USER");
        Reservation reservation = new Reservation(LocalDate.now().plusDays(1), user, reservationTime, theme);

        // when
        Reservation savedReservation = reservationRepository.save(reservation);

        // then
        assertThat(savedReservation.getId()).isEqualTo(3L);
        assertThat(savedReservation).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(reservation);
    }

    @Test
    @DisplayName("모든 예약을 조회한다.")
    void testFindAll() {
        List<Reservation> reservations = reservationRepository.findAll();

        assertThat(reservations).hasSize(2);
    }

    @Test
    void 예약을_검색한다() {
        // given
        Long userId = 1L;
        Long themeId = 1L;
        LocalDate dateFrom = LocalDate.of(2024, 6, 1);
        LocalDate dateTo = LocalDate.of(2024, 6, 30);

        // when
        List<Reservation> reservations = reservationRepository.findAllByUserIdAndThemeIdAndDateBetween(userId, themeId, dateFrom, dateTo);

        // then
        assertThat(reservations).hasSize(1);
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void testDeleteById() {
        reservationRepository.deleteById(1L);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation WHERE id = 1", Integer.class);
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("예약이 존재하면 true를 반환한다.")
    void testExistsById_Return_True() {
        boolean result = reservationRepository.existsById(1L);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("예약이 존재하지 않으면 false를 반환한다.")
    void testExistsById_Return_False() {
        boolean result = reservationRepository.existsById(5L);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("예약 시간이 존재하면 TRUE를 반환한다.")
    void existsByTimeId_ReturnTrue() {
        boolean result = reservationRepository.existsByReservationTimeId(1L);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("예약 시간이 존재하지 않으면 FALSE를 반환한다.")
    void existsByTimeId_ReturnFalse() {
        boolean result = reservationRepository.existsByReservationTimeId(2L);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("예약 날짜와 시간이 존재하면 TRUE를 반환한다.")
    void existsByDateAndTimeId_ReturnTrue() {
        boolean result = reservationRepository.existsByDateAndTimeId("2024-06-23", 1L);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("예약 날짜와 시간이 존재하지 않으면 FALSE를 반환한다.")
    void existsByDateAndTimeId_ReturnFalse() {
        boolean result = reservationRepository.existsByDateAndTimeId("2023-08-06", 1L);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("예약 날짜와 시간, 테마가 존재하면 TRUE를 반환한다.")
    void existsByDateAndReservationTimeAndTheme_ReturnTrue() {
        // given
        String date = "2024-06-23";
        Theme theme = new Theme(1L, "레벨1 탈출", "우테코 레벨1을 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.parse("10:00"));

        // when
        boolean result = reservationRepository.existsByDateAndReservationTimeAndTheme(date, reservationTime, theme);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("예약한 테마가 존재하지 않으면 FALSE를 반환한다.")
    void existsByDateAndReservationTimeAndTheme_ReturnFalse_WhenThemeNotFound() {
        // given
        String date = LocalDate.now().plusDays(1).toString();
        Theme theme = new Theme(2L, "레벨2 탈출", "우테코 레벨2을 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.parse("15:40"));

        // when
        boolean result = reservationRepository.existsByDateAndReservationTimeAndTheme(date, reservationTime, theme);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("예약한 시간이 존재하지 않으면 FALSE를 반환한다.")
    void existsByDateAndReservationTimeAndTheme_ReturnFalse_WhenTimeNotFound() {
        // given
        String date = LocalDate.now().plusDays(1).toString();
        Theme theme = new Theme(1L, "레벨1 탈출", "우테코 레벨1을 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ReservationTime reservationTime = new ReservationTime(2L, LocalTime.parse("15:40"));

        // when
        boolean result = reservationRepository.existsByDateAndReservationTimeAndTheme(date, reservationTime, theme);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 테마가_존재하면_TRUE를_반환한다() {
        boolean result = reservationRepository.existsByThemeId(1L);

        assertThat(result).isTrue();
    }

    @Test
    void 테마가_존재하지_않으면_FALSE를_반환한다() {
        boolean result = reservationRepository.existsByThemeId(5L);

        assertThat(result).isFalse();
    }
}
