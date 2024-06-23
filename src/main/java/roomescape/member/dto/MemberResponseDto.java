package roomescape.member.dto;

public class MemberResponseDto {

    private String name;

    public MemberResponseDto() {
    }

    public MemberResponseDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
