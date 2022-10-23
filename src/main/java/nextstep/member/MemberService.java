package nextstep.member;

import java.util.List;
import java.util.Objects;
import nextstep.auth.AuthenticationException;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private MemberDao memberDao;
    private ReservationDao reservationDao;

    public MemberService(MemberDao memberDao, ReservationDao reservationDao) {
        this.memberDao = memberDao;
        this.reservationDao = reservationDao;
    }

    public Long create(MemberRequest memberRequest) {
        return memberDao.save(memberRequest.toEntity());
    }

    public Member findById(Long id) {
        return memberDao.findById(id);
    }

    public List<Reservation> findAllReservationsByMemberId(
        Long memberId,
        Long loginMemberId
    ) {
        validateOwner(memberId, loginMemberId);
        return reservationDao.findAllByMemberId(memberId);
    }

    private void validateOwner(Long memberId, Long loginMemberId) {
        if (!Objects.equals(memberId, loginMemberId)) {
            throw new AuthenticationException();
        }
    }
}
