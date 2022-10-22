package nextstep.domain.service;

import java.util.List;
import java.util.Optional;
import nextstep.common.exception.ReservationException;
import nextstep.domain.Reservation;
import nextstep.domain.repository.ReservationRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationDomainService {

    private final ReservationRepository reservationRepository;

    public ReservationDomainService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Optional<Reservation> findOptionalBy(String date, String time) {
        return reservationRepository.findBy(date, time);
    }

    public Reservation findBy(String date, String time) {
        return reservationRepository.findBy(date, time)
            .orElseThrow(() -> new ReservationException("존재하지 않는 예약입니다."));
    }

    public List<Reservation> findAllBy(String date) {
        return reservationRepository.findAllBy(date);
    }

    public boolean exist(Long id) {
        return reservationRepository.exist(id);
    }

    public void save(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public void delete(String date, String time) {
        reservationRepository.delete(date, time);
    }

    public void deleteAll() {
        reservationRepository.deleteAll();
    }
}
