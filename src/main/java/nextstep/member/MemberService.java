package nextstep.member;

import java.util.List;
import java.util.Objects;
import nextstep.auth.AuthenticationException;
import nextstep.play.Play;
import nextstep.play.PlayDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberDao memberDao;
    private final ReservationDao reservationDao;
    private final PlayDao playDao;

    public MemberService(MemberDao memberDao, ReservationDao reservationDao, PlayDao playDao) {
        this.memberDao = memberDao;
        this.reservationDao = reservationDao;
        this.playDao = playDao;
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

    public List<Play> findAllPlaysByMemberId(Long memberId, Long loginMemberId) {
        validateOwner(memberId, loginMemberId);
        return playDao.findAllHiddenPlaysByMemberId(memberId);
    }

    private void validateOwner(Long memberId, Long loginMemberId) {
        if (!Objects.equals(memberId, loginMemberId)) {
            throw new AuthenticationException();
        }
    }
}
