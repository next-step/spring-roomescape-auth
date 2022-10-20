package nextstep.member;

import nextstep.auth.AuthenticationException;
import nextstep.support.DuplicateEntityException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long create(MemberRequest memberRequest) {
        checkUsernameAvailable(memberRequest.getUsername());
        return memberDao.save(memberRequest.toEntity());
    }

    public Member findById(Long id) {
        try {
            return memberDao.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new AuthenticationException("존재하지 않는 계정입니다.");
        }
    }

    private void checkUsernameAvailable(String username) {
        if (memberDao.existsByUsername(username)) {
            throw new DuplicateEntityException("이미 사용중인 username 입니다.");
        }
    }
}
