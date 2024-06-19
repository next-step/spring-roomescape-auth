package roomescape.application.port.in;

import java.util.List;
import roomescape.application.dto.MemberCommand;
import roomescape.application.dto.MemberResponse;

public interface MemberUseCase {

    List<MemberResponse> findMembers();

    void registerMember(MemberCommand memberCommand);
}
