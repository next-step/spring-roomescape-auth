package roomescape.apply.reservation.application;

import org.springframework.stereotype.Service;
import roomescape.apply.reservation.domain.Reservation;
import roomescape.apply.reservation.domain.repository.ReservationRepository;
import roomescape.apply.reservation.ui.dto.ReservationResponse;
import roomescape.apply.reservation.ui.dto.ReservationSearchParams;

import java.util.List;

@Service
public class ReservationSearcher {

    private final ReservationRepository reservationRepository;

    public ReservationSearcher(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    public List<ReservationResponse> searchReservations(ReservationSearchParams searchParams) {
        List<Reservation> searchedReservations = reservationRepository.searchReservationsBySearchParams(searchParams);
        return searchedReservations.stream().map(it -> ReservationResponse.from(it, it.getTheme(), it.getTime())).toList();
    }

}
