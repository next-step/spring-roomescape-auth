package nextstep.support;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BootingDataSetupRunner implements ApplicationRunner {

  private final JdbcTemplate jdbcTemplate;
  private final PasswordEncoder encoder;

  public BootingDataSetupRunner(JdbcTemplate jdbcTemplate,
      PasswordEncoder encoder) {
    this.jdbcTemplate = jdbcTemplate;
    this.encoder = encoder;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    var checkAdmin = """
        select count(*) from member where id = 1;
        """;
    Integer adminCount = jdbcTemplate.queryForObject(checkAdmin, Integer.class);

    if (adminCount > 0) {
      var insertAdmin = """
            insert into member (id, username, password_id, name, phone) values(1, '무적박살맨', 1, '성시형','01012345678');
          """;
      jdbcTemplate.update(insertAdmin);
    }

    var checkPassword = """
           select count(*) from password where id = 1;
        """;
    Integer passwordCount = jdbcTemplate.queryForObject(checkPassword, Integer.class);

    if (passwordCount > 0) {
      String admin12 = encoder.encode("admin12");
      var insertPassword = """
            insert into password (id, password) values(1, ?);
          """;
      jdbcTemplate.update(insertPassword, admin12);
    }
  }
}
