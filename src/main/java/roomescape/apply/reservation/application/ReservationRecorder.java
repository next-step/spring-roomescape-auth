package roomescape.apply.reservation.application;

import org.springframework.stereotype.Service;
import roomescape.apply.auth.ui.dto.LoginMember;
import roomescape.apply.member.application.MemberFinder;
import roomescape.apply.member.domain.Member;
import roomescape.apply.reservation.application.excpetion.DuplicateReservationException;
import roomescape.apply.reservation.domain.Reservation;
import roomescape.apply.reservation.domain.repository.ReservationRepository;
import roomescape.apply.reservation.ui.dto.ReservationAdminRequest;
import roomescape.apply.reservation.ui.dto.ReservationAdminResponse;
import roomescape.apply.reservation.ui.dto.ReservationRequest;
import roomescape.apply.reservation.ui.dto.ReservationResponse;
import roomescape.apply.reservationtime.application.ReservationTimeFinder;
import roomescape.apply.reservationtime.domain.ReservationTime;
import roomescape.apply.theme.application.ThemeFinder;
import roomescape.apply.theme.domain.Theme;

@Service
public class ReservationRecorder {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeFinder reservationTimeFinder;
    private final ThemeFinder themeFinder;
    private final ReservationFinder reservationFinder;
    private final MemberFinder memberFinder;

    public ReservationRecorder(ReservationRepository reservationRepository, ReservationTimeFinder reservationTimeFinder,
                               ThemeFinder themeFinder, ReservationFinder reservationFinder,
                               MemberFinder memberFinder) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeFinder = reservationTimeFinder;
        this.themeFinder = themeFinder;
        this.reservationFinder = reservationFinder;
        this.memberFinder = memberFinder;
    }

    public ReservationResponse recordReservationBy(ReservationRequest request, LoginMember loginMember) {
        validateNotDuplicateReservation(request.timeId(), request.themeId());

        final ReservationTime time = reservationTimeFinder.findOneById(request.timeId());
        final Theme theme = themeFinder.findOneById(request.themeId());
        final Reservation reservation = Reservation.of(loginMember.name(), request.date(), time, theme);
        final Reservation saved = reservationRepository.save(reservation);

        return ReservationResponse.from(saved, theme, time);
    }

    public ReservationAdminResponse recordReservationBy(ReservationAdminRequest request) {
        validateNotDuplicateReservation(request.timeId(), request.themeId());

        final Member member = memberFinder.findOneNameById(request.memberId());
        final ReservationTime time = reservationTimeFinder.findOneById(request.timeId());
        final Theme theme = themeFinder.findOneById(request.themeId());
        final Reservation reservation = Reservation.of(member.getName(), request.date(), time, theme);
        final Reservation saved = reservationRepository.save(reservation);

        return ReservationAdminResponse.from(saved, theme, time, member);
    }

    private void validateNotDuplicateReservation(long timeId, long themeId) {
        boolean alreadyExisted = reservationFinder.doesReservationExist(timeId, themeId);
        if (alreadyExisted) {
            throw new DuplicateReservationException();
        }
    }


}
