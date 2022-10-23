package nextstep;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DataLoader(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        jdbcTemplate.execute("INSERT INTO member (username, password, name, phone, role) "
                + "VALUES ('admin', 'admin_password', '어드민', '01011112222', 'ADMIN')");
    }
}
