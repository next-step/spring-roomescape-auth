package roomescape.apply.reservation.application;

import org.springframework.stereotype.Service;
import roomescape.apply.member.ui.dto.MemberResponse;
import roomescape.apply.reservation.domain.repository.ReservationRepository;
import roomescape.apply.reservation.ui.dto.ReservationAdminResponse;
import roomescape.apply.reservation.ui.dto.ReservationResponse;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationFinder {

    private final ReservationRepository reservationRepository;

    public ReservationFinder(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(it -> ReservationResponse.from(it, it.getTheme(), it.getTime()))
                .toList();
    }

    public List<ReservationAdminResponse> findAllForAdmin() {
        return reservationRepository.findAll()
                .stream()
                .map(it -> ReservationAdminResponse.from(it,
                        it.getTheme(),
                        it.getTime(),
                        MemberResponse.from(it.getMemberId(), it.getName()))
                )
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
