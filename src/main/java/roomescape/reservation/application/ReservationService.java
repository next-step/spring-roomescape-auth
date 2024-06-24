package roomescape.reservation.application;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.exception.PastDateReservationException;
import roomescape.reservation.exception.ReservationAlreadyExistsException;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.repository.ReservationTimeRepository;
import roomescape.user.domain.User;
import roomescape.user.domain.repository.UserRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.userRepository = userRepository;
    }

    public ReservationResponse save(ReservationCreateRequest request, Long memberId) {
        ReservationTime findReservationTime = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 예약 시간입니다."));
        Theme findTheme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 테마입니다."));
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));

        if (LocalDate.now().isAfter(LocalDate.parse(request.date()))) {
            throw new PastDateReservationException();
        }

        if (reservationRepository.existsByDateAndTimeId(request.date(), findReservationTime.getId())) {
            throw new ReservationAlreadyExistsException();
        }

        Reservation reservation = reservationRepository.save(request.toReservation(user, findReservationTime, findTheme));
        return ReservationResponse.from(reservation);
    }

    public List<ReservationResponse> getReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void cancelReservation(Long reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }
        reservationRepository.deleteById(reservationId);
    }
}
