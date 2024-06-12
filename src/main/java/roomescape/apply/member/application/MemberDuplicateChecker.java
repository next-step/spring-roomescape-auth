package roomescape.apply.member.application;

import org.springframework.stereotype.Service;
import roomescape.apply.member.ui.dto.MemberRequest;

@Service
public class MemberDuplicateChecker {

    private final MemberFinder memberFinder;

    public MemberDuplicateChecker(MemberFinder memberFinder) {
        this.memberFinder = memberFinder;
    }

    public void checkIsDuplicateEmail(MemberRequest request) {
        boolean isDuplicated = memberFinder.isDuplicateEmail(request.email());

        if (isDuplicated) {
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
        }
    }
}
