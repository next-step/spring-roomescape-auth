package nextstep.auth;

import java.sql.PreparedStatement;
import nextstep.member.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthDao {

  public final JdbcTemplate jdbcTemplate;

  private final RowMapper<Password> rowMapper = (resultSet, rowNum) -> new Password(
      resultSet.getLong("id"),
      resultSet.getString("password")
  );

  public AuthDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Long save(Password password) {
    String sql = "INSERT INTO password (password) VALUES (?);";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
      ps.setString(1, password.value());
      return ps;
    }, keyHolder);

    return keyHolder.getKey().longValue();
  }

  public Password findById(Long id) {
    String sql = "SELECT id, password from member where id = ?;";
    return jdbcTemplate.queryForObject(sql, rowMapper, id);
  }
}
