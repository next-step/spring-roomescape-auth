package roomescape.apply.member.domain;

import roomescape.support.domain.LongTypeIdentifier;

public class MemberId extends LongTypeIdentifier {

    public static MemberId of(Long id) {
        return new MemberId(id);
    }
    protected MemberId(Long id) {
        super(id);
    }
}
