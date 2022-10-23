package nextstep;

import nextstep.domain.reservation.usecase.ReservationRepository;
import nextstep.domain.reservation.InmemoryReservationRepository;
import nextstep.domain.theme.InmemoryThemeRepository;
import nextstep.domain.theme.usecase.ThemeRepository;
import nextstep.app.reservation.ReservationService;
import nextstep.app.theme.ThemeService;

public class Configuration {

    public static ReservationService getReservationService() {
        return new ReservationService(getInmemoryReservationRepository());
    }

    private static ReservationRepository getInmemoryReservationRepository() {
        return new InmemoryReservationRepository();
    }

    public static ThemeService getThemeService() {
        return new ThemeService(getInmemoryThemeRepository());
    }

    private static ThemeRepository getInmemoryThemeRepository() {
        return new InmemoryThemeRepository();
    }
}
