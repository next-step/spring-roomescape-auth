package nextstep.core.reservation.in;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationUseCase {
    ReservationResponse create(ReservationCreateRequest request);

    List<ReservationResponse> findReservations(Long themeId, LocalDate date);

    void delete(Long scheduleId, LocalDate date, LocalTime time);

    void delete(Long reservationId);
}
