package roomescape.application.port.in;

import java.util.List;
import roomescape.application.dto.ReservationTimeCommand;
import roomescape.application.dto.ReservationTimeResponse;

public interface ReservationTimeUseCase {

  ReservationTimeResponse registerReservationTime(ReservationTimeCommand reservationTimeCommand);

  List<ReservationTimeResponse> retrieveReservationTimes();

  void deleteReservationTime(Long id);

  List<ReservationTimeResponse> retrieveAvailableReservationTimes(String date, Long themeId);
}
