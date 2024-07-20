package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.*;
import roomescape.dto.reservation.ReservationsResponse;
import roomescape.dto.reservation.create.ReservationCreateRequest;
import roomescape.dto.reservation.create.ReservationCreateResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.custom.InvalidReservationTimeException;
import roomescape.exception.custom.ThemeNotFoundException;
import roomescape.exception.custom.UserNotFoundException;
import roomescape.repository.AuthRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final AuthRepository authRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository, AuthRepository authRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.authRepository = authRepository;
    }

    public List<ReservationsResponse> findReservations() {
        return reservationRepository.findReservations()
                .stream()
                .map(ReservationsResponse::new)
                .toList();
    }

    public ReservationCreateResponse createReservation(ReservationCreateRequest request) {
        checkReservationTime(request);
        ReservationTime time = reservationTimeRepository.findReservationTimeById(request.getTimeId());
        Theme theme = themeRepository.findThemeById(request.getThemeId())
                .orElseThrow(() -> new ThemeNotFoundException(ErrorCode.THEME_NOT_FOUND, "해당 테마가 존재하지 않습니다."));

//        if(user.getRole() == Role.ADMIN) {
//            User user = authRepository.findById(request.getUserId())
//                    .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, "해당 회원이 존재하지 않습니다."));
//        }

        Reservation reservation = reservationRepository.createReservation(request, time, theme);
        return ReservationCreateResponse.fromEntity(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteReservation(id);
    }

    public void checkReservationTime(ReservationCreateRequest request){
        //예약 시간
        ReservationTime reservationTime = reservationTimeRepository.findReservationTimeById(request.getTimeId());

        //예약 날짜
        LocalDate reservatedDate = LocalDate.parse(request.getDate());

        if (reservationTime.isBeforeCurrentTime(reservatedDate)) {
            throw new InvalidReservationTimeException(ErrorCode.INVALID_RESERVATION_TIME, "예약 시간을 다시 확인해주세요.");
        }
    }
}
