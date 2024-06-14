package roomescape.apply.reservationtime.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import roomescape.apply.reservation.domain.repository.ReservationJDBCRepository;
import roomescape.apply.reservation.domain.repository.ReservationRepository;
import roomescape.apply.reservationtime.domain.repository.ReservationTimeJDBCRepository;
import roomescape.apply.reservationtime.domain.repository.ReservationTimeRepository;
import roomescape.apply.reservationtime.ui.dto.ReservationTimeResponse;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.repository.ThemeJDBCRepository;
import roomescape.apply.theme.domain.repository.ThemeRepository;
import roomescape.support.BaseTestService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.ReservationsFixture.*;

class ReservationTimeFinderTest extends BaseTestService {

    private ReservationTimeFinder reservationTimeFinder;
    private ThemeRepository themeRepository;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        reservationTimeRepository = new ReservationTimeJDBCRepository(template);
        reservationRepository = new ReservationJDBCRepository(template);
        themeRepository = new ThemeJDBCRepository(template);

        reservationTimeFinder = new ReservationTimeFinder(reservationTimeRepository);
    }

    @AfterEach
    void clear() {
        transactionManager.rollback(transactionStatus);
    }

    @Test
    @DisplayName("저장한 예약 시간을 전부 가져온다.")
    void findAllTest() {
        // given
        List<String> times = List.of("10:00", "11:00", "12:00", "13:00", "14:00");
        for (String time : times) {
            reservationTimeRepository.save(reservationTime(time));
        }
        // when
        List<ReservationTimeResponse> responses = reservationTimeFinder.findAll();
        // then
        assertThat(responses).isNotEmpty().hasSize(times.size());
    }

    @Test
    @DisplayName("테마의 시간들을 예약 여부와 함께 가져온다.")
    void asd() {
        // given
        List.of("10:00", "12:00", "14:00", "16:00")
                .forEach(it -> reservationTimeRepository.save(reservationTime(it)));

        var reservationTime = reservationTime("18:00");
        reservationTimeRepository.save(reservationTime);
        String date = "2099-01-01";
        Theme savedTheme = themeRepository.save(theme());
        reservationRepository.save(reservation(reservationTime, savedTheme, date));
        // when
        var responses = reservationTimeFinder.findAvailableTimesBy(date, savedTheme.getId().toString());
        // then
        assertThat(responses).isNotEmpty().hasSize(5);
        assertThat(responses).extracting("startAt")
                .containsExactlyInAnyOrder("10:00", "12:00", "14:00", "16:00", "18:00");
        assertThat(responses).extracting("alreadyBooked")
                .containsExactlyInAnyOrder(false, false, false, false, true);
    }

}