package roomescape.application.port.in;

import java.util.List;
import roomescape.application.dto.ReservationCommand;
import roomescape.application.dto.ReservationResponse;

public interface ReservationUseCase {

  List<ReservationResponse> retrieveReservations();

  ReservationResponse registerReservation(ReservationCommand reservationCommand);

  void deleteReservation(Long id);
}
