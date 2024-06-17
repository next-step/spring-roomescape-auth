package roomescape.domain;

public class Reservation {
    private Long id;
    private String name;
    private String reservationDate;
    private ReservationTime time;
    private ReservationTheme theme;

    public Reservation() {
    }

    public Reservation(Long id, String name, String reservationDate, ReservationTime time, ReservationTheme theme) {
        this.id = id;
        this.name = name;
        this.reservationDate = reservationDate;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, String reservationDate, ReservationTime time, ReservationTheme theme) {
        this.name = name;
        this.reservationDate = reservationDate;
        this.time = time;
        this.theme = theme;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public ReservationTime getTime() {
        return time;
    }

    public ReservationTheme getTheme() {
        return theme;
    }

    public Reservation toEntity(Reservation reservation, Long id) {
        return new Reservation(id
                , reservation.getName()
                , reservation.getReservationDate()
                , reservation.getTime()
                , reservation.getTheme());
    }
}
