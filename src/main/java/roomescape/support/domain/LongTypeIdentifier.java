package roomescape.support.domain;

public class LongTypeIdentifier  {
    private Long id;
    protected LongTypeIdentifier(Long id) {
        this.id = id;
    }

    public Long longValue() {
        return id;
    }

    public Long nextValue() {
        return id + 1;
    }

}
