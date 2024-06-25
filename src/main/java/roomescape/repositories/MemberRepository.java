package roomescape.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRepository {

  private final JdbcTemplate jdbcTemplate;

  public Member save(String name, String email, String password){
    String sql = "INSERT INTO MEMBER (name, email, password) VALUES (?, ?, ?)";
    jdbcTemplate.update(sql, name, email, password);
  }
}
