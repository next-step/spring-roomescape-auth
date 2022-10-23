package nextstep.domain.reservation;

import nextstep.domain.reservation.usecase.ReservationRepository;

import java.util.*;

public class InmemoryReservationRepository implements ReservationRepository {
    List<Reservation> reservations = new ArrayList<>();

    @Override
    public Long save(Reservation reservation) {
        reservations.add(reservation);
        return (long) (reservations.size());
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return Optional.of(reservations.get(id.intValue()));
    }

    @Override
    public Optional<Reservation> findByScheduleId(Long scheduleId) {
        return reservations.stream()
                .filter(it -> Objects.equals(it.getSchedule(), scheduleId))
                .findFirst();
    }

    @Override
    public List<Reservation> findByMemberName(String name) {
        return Collections.emptyList();
    }

    @Override
    public List<Reservation> findAllBy(String date) {
        return null;
    }

    @Override
    public void delete(Long id) {
        reservations.remove(id);
    }
}
