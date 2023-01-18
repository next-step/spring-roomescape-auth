package nextstep.infra.noop;

import nextstep.core.reservation.Reservation;
import nextstep.core.reservation.out.ReservationRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ReservationInMemoryRepository implements ReservationRepository {
    private static final Map<Long, Reservation> RESERVATIONS = new ConcurrentHashMap<>();
    private final AtomicLong incrementor = new AtomicLong(1L);

    @Override
    public Reservation save(Reservation reservation) {
        Objects.requireNonNull(reservation);

        Reservation data = new Reservation(incrementor.getAndIncrement(), reservation.getMemberId());
        RESERVATIONS.put(data.getId(), data);
        return reservation;
    }

    @Override
    public List<Reservation> findAllByScheduleIdAndDate(Long scheduleId, LocalDate date) {
        Objects.requireNonNull(scheduleId);
        Objects.requireNonNull(date);

        return RESERVATIONS.values().stream()
                .filter(it -> it.isSameScheduleId(scheduleId))
                .toList();
    }

    @Override
    public void deleteById(Long reservationId) {
        Objects.requireNonNull(reservationId);

        RESERVATIONS.values().stream()
                .filter(it -> it.isSameReservationId(reservationId))
                .map(Reservation::getId)
                .forEach(RESERVATIONS::remove);
    }

    @Override
    public boolean existsById(Long reservationId) {
        Objects.requireNonNull(reservationId);

        Optional<Reservation> reservation = RESERVATIONS.values().stream()
                .filter(it -> it.isSameReservationId(reservationId))
                .findFirst();
        return reservation.isPresent();
    }
}
