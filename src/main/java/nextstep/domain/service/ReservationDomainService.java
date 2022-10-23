package nextstep.domain.service;

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

    public void save(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public Reservation findBy(Long scheduleId, Long memberId) {
        return reservationRepository.findBy(scheduleId, memberId)
            .orElseThrow(() -> new ReservationException("존재하지 않는 예약입니다."));
    }

    public void deleteBy(Long id) {
        reservationRepository.deleteBy(id);
    }

    public void deleteAll() {
        reservationRepository.deleteAll();
    }
}
