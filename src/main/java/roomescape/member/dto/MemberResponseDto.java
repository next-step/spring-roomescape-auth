package roomescape.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MemberResponseDto {

    private String name;

    public MemberResponseDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
