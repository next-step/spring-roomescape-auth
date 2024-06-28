package roomescape.member.dto;

public class MemberRequestDto {

    private String name;

    public MemberRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
