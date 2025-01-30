package roomescape.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import roomescape.domain.reservation.infra.ReservationJdbcRepository;
import roomescape.domain.reservationtime.infra.ReservationTimeJdbcRepository;
import roomescape.domain.theme.infra.ThemeJdbcRepository;
import roomescape.domain.user.infra.UserJdbcRepository;

@Component
public class DataCleanser {

    @Autowired
    ReservationTimeJdbcRepository reservationTimeRepository;

    @Autowired
    ReservationJdbcRepository reservationRepository;

    @Autowired
    ThemeJdbcRepository themeRepository;

    @Autowired
    UserJdbcRepository userRepository;

    public void clean() {
        userRepository.deleteAllInBatch();
        reservationRepository.deleteAllInBatch();
        reservationTimeRepository.deleteAllInBatch();
        themeRepository.deleteAllInBatch();
    }
}
