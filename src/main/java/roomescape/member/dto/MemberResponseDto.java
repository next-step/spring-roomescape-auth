package roomescape.member.dto;

public class MemberResponseDto {
   private String name;

    public String getName() {
        return name;
    }

    public MemberResponseDto() {
    }

    public MemberResponseDto(String name) {
        this.name = name;
    }
}
