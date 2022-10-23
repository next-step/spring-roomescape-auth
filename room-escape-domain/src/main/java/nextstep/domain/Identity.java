package nextstep.domain;

public class Identity {
    public Long getNumber() {
        return number;
    }

    private final Long number;

    public Identity(Long number) {
        this.number = number;
    }

    public static Identity nothing() {
        return new Identity(Long.MIN_VALUE);
    }
}
