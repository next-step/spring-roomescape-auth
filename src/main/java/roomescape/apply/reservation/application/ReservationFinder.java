package roomescape.apply.reservation.application;

import org.springframework.stereotype.Service;
import roomescape.apply.auth.ui.dto.LoginMember;
import roomescape.apply.member.application.MemberFinder;
import roomescape.apply.member.domain.Member;
import roomescape.apply.reservation.domain.repository.ReservationRepository;
import roomescape.apply.reservation.ui.dto.ReservationAdminResponse;
import roomescape.apply.reservation.ui.dto.ReservationResponse;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationFinder {

    private final ReservationRepository reservationRepository;
    private final MemberFinder memberFinder;

    public ReservationFinder(ReservationRepository reservationRepository, MemberFinder memberFinder) {
        this.reservationRepository = reservationRepository;
        this.memberFinder = memberFinder;
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(it -> ReservationResponse.from(it, it.getTheme(), it.getTime()))
                .toList();
    }

    public List<ReservationAdminResponse> findAllForAdmin(LoginMember loginMember) {
        Member member = memberFinder.findOneNameById(loginMember.id());
        return reservationRepository.findAll()
                .stream()
                .map(it -> ReservationAdminResponse.from(it, it.getTheme(), it.getTime(), member))
                .toList();
    }

    public boolean doesReservationExist(long timeId, long themeId) {
        Optional<Long> existedId = reservationRepository.findIdByTimeIdAndThemeId(timeId, themeId);
        return existedId.isPresent();
    }

    public Optional<Long> findIdByTimeId(long timeId) {
        return reservationRepository.findIdByTimeId(timeId);
    }

    public Optional<Long> findIdByThemeId(long themeId) {
        return reservationRepository.findIdByThemeId(themeId);
    }
}
