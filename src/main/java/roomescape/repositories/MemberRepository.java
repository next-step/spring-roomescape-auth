package roomescape.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.entities.Member;

@RequiredArgsConstructor
@Repository
public class MemberRepository {

  private final JdbcTemplate jdbcTemplate;

  public Member save(String name, String email, String password, String role){
    String sql = "INSERT INTO MEMBER (name, email, password) VALUES (?, ?, ?, ?)";
    jdbcTemplate.update(sql, name, email, password);
    return Member.builder()
      .name(name)
      .email(email)
      .password(password)
      .role(role)
      .build();
  }

  public Member findByEmail(String email){
    String sql = "SELECT * FROM MEMBER WHERE email = ?";
    return jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) ->
      Member.builder()
        .name(rs.getString("name"))
        .email(rs.getString("email"))
        .password(rs.getString("password"))
        .role(rs.getString("role"))
        .build());
  }
}
