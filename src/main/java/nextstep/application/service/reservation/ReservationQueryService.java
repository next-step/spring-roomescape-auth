package nextstep.application.service.reservation;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.domain.Reservation;
import nextstep.domain.service.ReservationDomainService;
import nextstep.application.dto.reservation.ReservationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ReservationQueryService {

    private final ReservationDomainService reservationDomainService;

    public ReservationQueryService(ReservationDomainService reservationDomainService) {
        this.reservationDomainService = reservationDomainService;
    }

    public List<ReservationResponse> checkAll(String date) {
        List<Reservation> reservations = reservationDomainService.findAllBy(date);

        if (reservations.isEmpty()) {
            return Collections.emptyList();
        }
        return reservations.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getDate().toString(),
            reservation.getTime().toString(),
            reservation.getName()
        );
    }

    public boolean exist(Long id) {
        return reservationDomainService.exist(id);
    }
}
