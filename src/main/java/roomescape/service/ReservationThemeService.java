package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTheme;
import roomescape.dto.request.ReservationThemeRequest;
import roomescape.dto.response.ReservationThemeResponse;
import roomescape.exception.custom.DuplicateThemeException;
import roomescape.exception.custom.ReservationThemeConflictException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationThemeDao;

@Service
public class ReservationThemeService {

    private final ReservationThemeDao reservationThemeDao;
    private final ReservationDao reservationDao;

    public ReservationThemeService(ReservationThemeDao reservationThemeDao, ReservationDao reservationDao) {
        this.reservationThemeDao = reservationThemeDao;
        this.reservationDao = reservationDao;
    }

    public ReservationThemeResponse createReservationTheme(ReservationThemeRequest request) {
        Long count = reservationThemeDao.findByName(request.getName());
        if (count > 0) {
            throw new DuplicateThemeException();
        }

        ReservationTheme reservationTheme = reservationThemeDao.save(this.convertToEntity(request));
        return this.convertToResponse(reservationTheme);
    }

    public List<ReservationThemeResponse> findAllReservationThemes() {
        return reservationThemeDao.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public void deleteReservationTheme(Long id) {
        long count = reservationDao.countByThemeId(id);
        if (count > 0) {
            throw new ReservationThemeConflictException();
        }

        reservationThemeDao.delete(id);
    }

    private ReservationThemeResponse convertToResponse(ReservationTheme reservationTheme) {
        return new ReservationThemeResponse(
                reservationTheme.getId()
                , reservationTheme.getName()
                , reservationTheme.getDescription()
                , reservationTheme.getThumbnail());
    }

    private ReservationTheme convertToEntity(ReservationThemeRequest reservationThemeRequest) {
        return new ReservationTheme(
                reservationThemeRequest.getName()
                , reservationThemeRequest.getDescription()
                , reservationThemeRequest.getThumbnail());
    }
}
