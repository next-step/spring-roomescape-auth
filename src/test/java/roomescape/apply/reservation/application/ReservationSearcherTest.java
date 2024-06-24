package roomescape.apply.reservation.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import roomescape.apply.member.domain.Member;
import roomescape.apply.member.domain.repository.MemberJDBCRepository;
import roomescape.apply.member.domain.repository.MemberRepository;
import roomescape.apply.reservation.domain.repository.ReservationJDBCRepository;
import roomescape.apply.reservation.domain.repository.ReservationRepository;
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

class ReservationSearcherTest extends BaseTestService {

    private ReservationSearcher reservationSearcher;
    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;

    private static final String DEFAULT_DATE = "2099-01-01";
    private static final List<String> DEFAULT_FIVE_RESERVATION_TIMES = List.of("10:00", "12:00", "14:00", "16:00", "18:00");

    @BeforeEach
    void setUp() {
        transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        themeRepository = new ThemeJDBCRepository(template);
        memberRepository = new MemberJDBCRepository(template);
        reservationRepository = new ReservationJDBCRepository(template);
        reservationTimeRepository = new ReservationTimeJDBCRepository(template);

        reservationSearcher = new ReservationSearcher(reservationRepository);
    }

    @AfterEach
    void clear() {
        transactionManager.rollback(transactionStatus);
    }

    @Test
    @DisplayName("예약을 사용자로 검색해서 가져 올 수 있다.")
    void searchReservationsTest1() {
        // given
        Long memberOneId = saveMember().getId();
        Long memberTwoId = saveMember().getId();
        Theme defaultTheme = saveTheme("기본 테마", "기본 설명");
        Theme targetTheme = saveTheme("서치_테스트", "테스트입니다.");
        List<String> shortTimes = List.of("10:00", "12:00", "14:00");

        // when
        saveReservations(DEFAULT_FIVE_RESERVATION_TIMES, memberOneId, defaultTheme, DEFAULT_DATE);
        saveReservations(shortTimes, memberTwoId, targetTheme, DEFAULT_DATE);

        // then
        var memberOneResult = reservationSearcher.searchReservations(reservationSearchParams(memberOneId));
        assertThat(memberOneResult).hasSize(DEFAULT_FIVE_RESERVATION_TIMES.size());
        var memberTwoResult = reservationSearcher.searchReservations(reservationSearchParams(memberTwoId));
        assertThat(memberTwoResult).hasSize(shortTimes.size());
    }

    @Test
    @DisplayName("예약을 테마로 검색해서 가져 올 수 있다.")
    void searchReservationsTest2() {
        // given
        Long memberOneId = saveMember().getId();
        Theme defaultTheme = saveTheme("기본 테마", "기본 설명");
        Theme targetTheme = saveTheme("서치_테스트", "테스트입니다.");
        List<String> shortTimes = List.of("10:00", "12:00", "14:00");

        // when
        saveReservations(DEFAULT_FIVE_RESERVATION_TIMES, memberOneId, defaultTheme, DEFAULT_DATE);
        saveReservations(shortTimes, memberOneId, targetTheme, DEFAULT_DATE);

        // then
        var memberOneResult = reservationSearcher.searchReservations(reservationSearchParamsThemeId(targetTheme.getId()));
        assertThat(memberOneResult).hasSize(shortTimes.size());
    }

    @Test
    @DisplayName("예약을 사용자와 테마로 검색해서 가져 올 수 있다.")
    void searchReservationsTest3() {
        // given
        Long memberOneId = saveMember().getId();
        Long targetMemberId = saveMember().getId();
        Theme defaultTheme = saveTheme("기본 테마", "기본 설명");
        Theme targetTheme = saveTheme("서치_테스트", "테스트입니다.");
        List<String> targetThemeTimes = List.of("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00");
        List<String> targetReservedTimes = List.of("08:00", "10:00", "12:00", "14:00");

        // when
        saveReservations(DEFAULT_FIVE_RESERVATION_TIMES, memberOneId, defaultTheme, DEFAULT_DATE);

        for (String time : targetThemeTimes) {
            ReservationTime saveReservationTime = saveReservationTime(time);
            if (targetReservedTimes.contains(time)) {
                reservationRepository.save(reservation(saveReservationTime, targetTheme, DEFAULT_DATE, targetMemberId));
            } else {
                reservationRepository.save(reservation(saveReservationTime, targetTheme, DEFAULT_DATE, memberOneId));
            }
        }

        // then
        var searchParams = reservationSearchParams(targetTheme.getId(), targetMemberId);
        var memberOneResult = reservationSearcher.searchReservations(searchParams);
        assertThat(memberOneResult).hasSize(targetReservedTimes.size());
    }

    @Test
    @DisplayName("예약을 시작 기간으로 검색해서 가져 올 수 있다.")
    void searchReservationsTest4() {
        // given
        Long memberOneId = saveMember().getId();
        Theme defaultTheme = saveTheme("기본 테마", "기본 설명");
        List<String> reservedDates = List.of("2099-01-01", "2099-01-02", "2099-01-03", "2099-01-04", "2099-01-05");

        // when
        for (String reservedDate : reservedDates) {
            saveReservations(List.of("10:00"), memberOneId, defaultTheme, reservedDate);
        }

        // then
        var searchResultOne = reservationSearcher.searchReservations(reservationSearchParams("2099-01-02"));
        assertThat(searchResultOne).hasSize(4);
        var searchResultTwo = reservationSearcher.searchReservations(reservationSearchParams("2099-01-03"));
        assertThat(searchResultTwo).hasSize(3);
        var searchResultThree = reservationSearcher.searchReservations(reservationSearchParams("2099-01-05"));
        assertThat(searchResultThree).hasSize(1);
    }

    @Test
    @DisplayName("예약을 종료 기간으로 검색해서 가져 올 수 있다.")
    void searchReservationsTest5() {
        // given
        Long memberOneId = saveMember().getId();
        Theme defaultTheme = saveTheme("기본 테마", "기본 설명");
        List<String> reservedDates = List.of("2099-01-01", "2099-01-02", "2099-01-03", "2099-01-04", "2099-01-05");

        // when
        for (String reservedDate : reservedDates) {
            saveReservations(List.of("10:00"), memberOneId, defaultTheme, reservedDate);
        }

        // then
        var searchResultOne = reservationSearcher.searchReservations(reservationSearchParamsDateTo("2099-01-02"));
        assertThat(searchResultOne).hasSize(2);
        var searchResultTwo = reservationSearcher.searchReservations(reservationSearchParamsDateTo("2099-01-03"));
        assertThat(searchResultTwo).hasSize(3);
        var searchResultThree = reservationSearcher.searchReservations(reservationSearchParamsDateTo("2099-01-05"));
        assertThat(searchResultThree).hasSize(5);
    }

    @Test
    @DisplayName("예약을 시작과 종료 기간으로 검색해서 가져 올 수 있다.")
    void searchReservationsTest6() {
        // given
        Long memberOneId = saveMember().getId();
        Theme defaultTheme = saveTheme("기본 테마", "기본 설명");
        List<String> reservedDates = List.of("2099-01-01", "2099-01-02", "2099-01-03", "2099-01-04", "2099-01-05");

        // when
        for (String reservedDate : reservedDates) {
            saveReservations(List.of("10:00"), memberOneId, defaultTheme, reservedDate);
        }

        // then
        var searchParamsOne = reservationSearchParams("2099-01-01", "2099-01-05");
        var searchResultOne = reservationSearcher.searchReservations(searchParamsOne);
        assertThat(searchResultOne).hasSize(5);
        var searchParamsTwo = reservationSearchParams("2099-01-02", "2099-01-04");
        var searchResultTwo = reservationSearcher.searchReservations(searchParamsTwo);
        assertThat(searchResultTwo).hasSize(3);
        var searchParamsThree = reservationSearchParams("2099-01-03", "2099-01-03");
        var searchResultThree = reservationSearcher.searchReservations(searchParamsThree);
        assertThat(searchResultThree).hasSize(1);
    }

    @Test
    @DisplayName("예약을 사용자와 테마 그리고 시작, 종료 기간으로 검색해서 가져 올 수 있다.")
    void searchReservationsTest7() {
        // given
        Long memberOneId = saveMember().getId();
        Long targetMemberId = saveMember().getId();
        Theme defaultTheme = saveTheme("기본 테마", "기본 설명");
        Theme targetTheme = saveTheme("서치_테스트", "테스트입니다.");
        List<String> targetThemeTimes = List.of("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00");
        List<String> targetReservedTimes = List.of("08:00", "10:00", "12:00", "14:00");
        List<String> reservedDates = List.of("2099-01-01", "2099-01-02", "2099-01-03", "2099-01-04", "2099-01-05");

        // when
        for (String reservedDate : reservedDates) {
            saveReservations(DEFAULT_FIVE_RESERVATION_TIMES, memberOneId, defaultTheme, reservedDate);
            for (String time : targetThemeTimes) {
                ReservationTime saveReservationTime = saveReservationTime(time);
                if (targetReservedTimes.contains(time)) {
                    reservationRepository.save(reservation(saveReservationTime, targetTheme, reservedDate, targetMemberId));
                } else {
                    reservationRepository.save(reservation(saveReservationTime, targetTheme, reservedDate, memberOneId));
                }
            }
        }

        // then
        var searchParamOne = reservationSearchParams(targetTheme.getId(),
                targetMemberId,
                "2099-01-01",
                "2099-01-04"
        );
        var searchResultOne = reservationSearcher.searchReservations(searchParamOne);
        assertThat(searchResultOne).hasSize(targetReservedTimes.size() * 4);
        var searchParamTwo = reservationSearchParams(targetTheme.getId(),
                targetMemberId,
                "2099-01-04",
                "2099-01-05"
        );
        var searchResultTwo = reservationSearcher.searchReservations(searchParamTwo);
        assertThat(searchResultTwo).hasSize(targetReservedTimes.size() * 2);
    }

    private Member saveMember() {
        return memberRepository.save(member());
    }

    private Theme saveTheme(String name, String description) {
        return themeRepository.save(theme(name, description));
    }

    private ReservationTime saveReservationTime(String time) {
        return reservationTimeRepository.save(reservationTime(time));
    }

    private void saveReservations(List<String> times, Long memberId, Theme theme, String date) {
        for (String time : times) {
            ReservationTime saveReservationTime = saveReservationTime(time);
            reservationRepository.save(reservation(saveReservationTime, theme, date, memberId));
        }
    }
}
