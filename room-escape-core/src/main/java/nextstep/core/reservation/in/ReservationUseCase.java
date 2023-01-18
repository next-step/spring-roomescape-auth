package nextstep.core.reservation.in;

import java.time.LocalDate;
import java.util.List;

public interface ReservationUseCase {
    ReservationResponse create(ReservationCreateRequest request, Long memberId);

    List<ReservationResponse> findReservations(Long themeId, LocalDate date);

    void delete(Long reservationId, Long memberId);
}
