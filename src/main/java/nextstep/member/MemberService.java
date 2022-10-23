package nextstep.member;

import java.util.List;
import nextstep.auth.AuthService;
import nextstep.auth.PasswordCreateRequest;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

  private MemberDao memberDao;
  private AuthService authService;

  public MemberService(MemberDao memberDao, AuthService authService) {
    this.memberDao = memberDao;
    this.authService = authService;
  }

  public Long create(MemberRequest memberRequest) {
    Long passwordId = authService.createPassword(new PasswordCreateRequest(memberRequest.getPassword()));
    return memberDao.save(memberRequest.toEntity(passwordId));
  }

  public Member findById(String id) {
    return findById(Long.valueOf(id));
  }

  public Member findById(Long id) {
    Member member = memberDao.findById(id);
    List<MemberRole> roles = memberDao.findMemberRoleByMemberId(id);
    member.setRoles(roles);

    return member;
  }

  public Member findByUsername(String name) {
    return memberDao.findByUsername(name);
  }
}
