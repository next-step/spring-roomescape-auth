package nextstep.member;

import nextstep.support.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

  private final MemberDao memberDao;

  public MemberService(MemberDao memberDao) {
    this.memberDao = memberDao;
  }

  public Long create(MemberRequest memberRequest) {
    return memberDao.save(memberRequest.toEntity());
  }

  public Member findById(Long id) {
    return memberDao.findById(id);
  }

  public Member getByUserNameAndPassword(String username, String password) {
    var member = memberDao.findByUsername(username);
    if (member.checkWrongPassword(password)) {
      throw new BadRequestException("패스워드가 일치하지 않아요");
    }
    return member;
  }
}
