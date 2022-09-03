package nextstep.reservation;

public class ReservationRequest {
    private Long themeId;
    private String date;
    private String time;
    private String name;

    public ReservationRequest(Long themeId, String date, String time, String name) {
        this.themeId = themeId;
        this.date = date;
        this.time = time;
        this.name = name;
    }

    public Long getThemeId() {
        return themeId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}
