package roomescape.reservationTheme.ui;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservationTheme.dto.ReservationThemeRequestDto;
import roomescape.reservationTheme.dto.ReservationThemeResponseDto;
import roomescape.reservationTheme.application.ReservationThemeService;

import java.util.List;

@RestController
@RequestMapping("/themes")
public class ReservationThemeController {

    private static final Logger log = LoggerFactory.getLogger(ReservationThemeController.class);
    private final ReservationThemeService reservationThemeService;

    public ReservationThemeController(ReservationThemeService reservationThemeService) {
        this.reservationThemeService = reservationThemeService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationThemeResponseDto>> getThemes() {
        final List<ReservationThemeResponseDto> themese = reservationThemeService.getThemes();
        return ResponseEntity.ok(themese);
    }

    @PostMapping
    public ResponseEntity<ReservationThemeResponseDto> createTheme(@Valid @RequestBody ReservationThemeRequestDto requestDto) {
        final ReservationThemeResponseDto theme = reservationThemeService.createTheme(requestDto);
        HttpHeaders header = new HttpHeaders();
        header.add("Location", "/theme/" + theme.getId());
        return new ResponseEntity<>(theme, header, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        reservationThemeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
