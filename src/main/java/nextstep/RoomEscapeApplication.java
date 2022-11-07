package nextstep;

import nextstep.member.Member;
import nextstep.member.MemberDao;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class RoomEscapeApplication implements ApplicationRunner {
    private final MemberDao memberDao;

    public RoomEscapeApplication(JdbcTemplate jdbcTemplate) {
        this.memberDao = new MemberDao(jdbcTemplate);
    }

    public static void main(String[] args) {
        SpringApplication.run(RoomEscapeApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        final Member admin = new Member("admin", "admin", "관리자", "010-0000-0000", "ADMIN");
        memberDao.save(admin);
    }
}
