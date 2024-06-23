package roomescape.theme.service;

import org.springframework.stereotype.Service;
import roomescape.error.exception.ThemeNotExistsException;
import roomescape.error.exception.ThemeReferenceException;
import roomescape.reservation.service.ReservationRepository;

import java.util.List;
import java.util.stream.Collectors;
import roomescape.theme.Theme;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(ThemeRepository themeRepository,
                        ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ThemeResponse> findThemes() {
        return themeRepository.find().stream()
            .map(ThemeResponse::new)
            .collect(Collectors.toList());
    }

    public Long saveThemes(ThemeRequest request) {
        return themeRepository.save(request.getName(), request.getDescription(),
            request.getThumbnail());
    }

    public ThemeResponse findTheme(Long id) {
        Theme theme = themeRepository.findById(id).orElseThrow(ThemeNotExistsException::new);
        return new ThemeResponse(theme);
    }

    public void deleteTheme(long id) {
        if (reservationRepository.countByTheme(id) > 0) {
            throw new ThemeReferenceException();
        }

        themeRepository.delete(id);
    }
}
