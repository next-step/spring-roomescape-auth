package roomescape.domain.member.service.dto;

public class MemberResponse {

    private final Long id;
    private final String name;

    public MemberResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
