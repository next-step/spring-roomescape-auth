package nextstep.member;

public record MemberResponse(Long id,
                             String username,
                             String password,
                             String name,
                             String phone,
                             String role) {
}
