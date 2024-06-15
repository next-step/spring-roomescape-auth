package roomescape.apply.reservation.application;

import org.junit.jupiter.api.*;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import roomescape.apply.member.application.MemberFinder;
import roomescape.apply.member.application.MemberRoleFinder;
import roomescape.apply.member.application.mock.MockPasswordHasher;
import roomescape.apply.member.domain.repository.MemberJDBCRepository;
import roomescape.apply.member.domain.repository.MemberRoleJDBCRepository;
import roomescape.apply.reservation.application.excpetion.DuplicateReservationException;
import roomescape.apply.reservation.domain.repository.ReservationJDBCRepository;
import roomescape.apply.reservation.ui.dto.ReservationRequest;
import roomescape.apply.reservation.ui.dto.ReservationResponse;
import roomescape.apply.reservationtime.application.ReservationTimeFinder;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.repository.ReservationTimeJDBCRepository;
import roomescape.apply.reservationtime.domain.repository.ReservationTimeRepository;
import roomescape.apply.theme.application.ThemeFinder;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.repository.ThemeJDBCRepository;
import roomescape.apply.theme.domain.repository.ThemeRepository;
import roomescape.support.BaseTestService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.support.MemberFixture.loginMember;
import static roomescape.support.ReservationsFixture.*;

class ReservationRecorderTest extends BaseTestService {

    private ReservationRecorder reservationRecorder;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        reservationTimeRepository = new ReservationTimeJDBCRepository(template);
        var reservationRepository = new ReservationJDBCRepository(template);
        themeRepository = new ThemeJDBCRepository(template);
        var memberRepository = new MemberJDBCRepository(template);
        var memberRoleRepository = new MemberRoleJDBCRepository(template);

        var themeFinder = new ThemeFinder(themeRepository);
        var reservationTimeFinder = new ReservationTimeFinder(reservationTimeRepository);
        var memberRoleFinder = new MemberRoleFinder(memberRoleRepository);
        var memberFinder = new MemberFinder(new MockPasswordHasher(), memberRepository, memberRoleFinder);
        var reservationFinder = new ReservationFinder(reservationRepository, memberFinder);
        reservationRecorder = new ReservationRecorder(reservationRepository,
                reservationTimeFinder,
                themeFinder,
                reservationFinder,
                memberFinder);
    }

    @AfterEach
    void clear() {
        transactionManager.rollback(transactionStatus);
    }

    @Test
    @DisplayName("신규 예약 저장 테스트")
    void recordReservationBy() {
        // given
        ReservationTime time = reservationTimeRepository.save(reservationTime());
        Theme theme = themeRepository.save(theme());
        ReservationRequest request = reservationRequest(time.getId(), theme.getId());
        // when
        ReservationResponse response = reservationRecorder.recordReservationBy(request, loginMember());
        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isNotZero();
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("id", "time", "theme")
                .isEqualTo(request);
        assertThat(response.time()).usingRecursiveComparison()
                .isEqualTo(time);
        assertThat(response.theme()).usingRecursiveComparison()
                .isEqualTo(theme);
    }

    @Test
    @DisplayName("이미 예약된 시간의 테마는 예약할 수 없어야한다.")
    void notDuplicatedTimeAndTheme() {
        // given
        ReservationTime time = reservationTimeRepository.save(reservationTime());
        Theme theme = themeRepository.save(theme());
        ReservationRequest request = reservationRequest(time.getId(), theme.getId());
        // when && then
        Assertions.assertDoesNotThrow(() -> reservationRecorder.recordReservationBy(request, loginMember()));
        assertThatThrownBy(() -> reservationRecorder.recordReservationBy(request, loginMember()))
                .isInstanceOf(DuplicateReservationException.class)
                .hasMessage(DuplicateReservationException.DEFAULT_MESSAGE);
    }

}