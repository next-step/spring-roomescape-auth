package roomescape.domain.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.service.dto.ReservationRequest;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.domain.repository.ReservationRepository;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.theme.service.ThemeService;
import roomescape.domain.time.domain.Time;
import roomescape.domain.time.service.TimeService;

import java.util.List;

@Service
public class ReservationService {

    private final TimeService timeService;
    private final ThemeService themeService;
    private final ReservationRepository reservationRepository;

    public ReservationService(TimeService timeService, ThemeService themeService, ReservationRepository reservationRepository) {
        this.timeService = timeService;
        this.themeService = themeService;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Reservation save(ReservationRequest reservationRequest) {
        Time time = timeService.findById(reservationRequest.getTimeId());
        Theme theme = themeService.findById(reservationRequest.getThemeId());
        Reservation reservation = new Reservation(null, reservationRequest.getName(), reservationRequest.getDate(), time, theme);
        long id = reservationRepository.save(reservation);
        return findById(id);
    }

    @Transactional(readOnly = true)
    public Reservation findById(long id) {
        return reservationRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        reservationRepository.delete(id);
    }
}
