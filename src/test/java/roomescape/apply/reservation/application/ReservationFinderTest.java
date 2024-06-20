package roomescape.apply.reservation.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import roomescape.apply.member.application.MemberFinder;
import roomescape.apply.member.application.MemberRoleFinder;
import roomescape.apply.member.application.mock.MockPasswordHasher;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.repository.MemberJDBCRepository;
import roomescape.apply.member.domain.repository.MemberRepository;
import roomescape.apply.member.domain.repository.MemberRoleJDBCRepository;
import roomescape.apply.reservation.domain.repository.ReservationJDBCRepository;
import roomescape.apply.reservation.domain.repository.ReservationRepository;
import roomescape.apply.reservation.ui.dto.ReservationResponse;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.reservationtime.domain.repository.ReservationTimeJDBCRepository;
import roomescape.apply.reservationtime.domain.repository.ReservationTimeRepository;
import roomescape.apply.theme.domain.Theme;
import roomescape.apply.theme.domain.repository.ThemeJDBCRepository;
import roomescape.apply.theme.domain.repository.ThemeRepository;
import roomescape.support.BaseTestService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.support.MemberFixture.member;
import static roomescape.support.ReservationsFixture.*;

class ReservationFinderTest extends BaseTestService {

    private ReservationFinder reservationFinder;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        reservationRepository = new ReservationJDBCRepository(template);
        reservationTimeRepository = new ReservationTimeJDBCRepository(template);
        themeRepository = new ThemeJDBCRepository(template);
        memberRepository = new MemberJDBCRepository(template);

        var memberRepository = new MemberJDBCRepository(template);
        var memberRoleRepository = new MemberRoleJDBCRepository(template);

        var memberRoleFinder = new MemberRoleFinder(memberRoleRepository);
        var memberFinder = new MemberFinder(new MockPasswordHasher(), memberRepository, memberRoleFinder);
        reservationFinder = new ReservationFinder(reservationRepository, memberFinder);
    }

    @AfterEach
    void clear() {
        transactionManager.rollback(transactionStatus);
    }

    @Test
    @DisplayName("기록한 예약들을 전부 가져온다.")
    void findAllTest() {
        // given
        Member saveMember = memberRepository.save(member());
        List<String> times = List.of("10:00", "11:00", "12:00", "13:00", "14:00");
        for (String time : times) {
            ReservationTime saveReservationTime = reservationTimeRepository.save(reservationTime(time));
            Theme saveTheme = themeRepository.save(theme());
            reservationRepository.save(reservation(saveReservationTime, saveTheme, "2099-01-01", saveMember.getId()));
        }
        // when
        List<ReservationResponse> responses = reservationFinder.findAll();
        // then
        assertThat(responses).isNotEmpty().hasSize(times.size());
    }

}
