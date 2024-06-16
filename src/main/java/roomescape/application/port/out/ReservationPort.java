package roomescape.application.port.out;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public interface ReservationPort {

  List<Reservation> findReservations();

  Optional<Reservation> findReservationByReservationTime(ReservationTime reservationTime);

  Reservation saveReservation(Reservation reservation);

  void deleteReservation(Long id);

  Integer countReservationById(Long id);
}
