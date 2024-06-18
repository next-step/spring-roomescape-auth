package roomescape.repository;

import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

	private static final Logger logger = LoggerFactory.getLogger(MemberRepository.class);

	private static final RowMapper<Member> MEMBER_ROW_MAPPER;

	private final JdbcTemplate jdbcTemplate;

	private SimpleJdbcInsert jdbcInsert;

	MemberRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@PostConstruct
	public void init() {
		this.jdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName("member")
			.usingGeneratedKeyColumns("id");
	}

	public boolean isExists(String name) {
		String sql = "SELECT COUNT(*) FROM member WHERE name = ?";
		Integer count = this.jdbcTemplate.queryForObject(sql, Integer.class, name);
		return count != null && count > 0;
	}

	public Member save(Member member) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("name", member.getName());
		parameters.put("email", member.getEmail());
		parameters.put("password", member.getPassword());
		parameters.put("role", MemberRole.USER.name());

		Number generatedId = this.jdbcInsert.executeAndReturnKey(parameters);
		member.setId(generatedId.longValue());
		return member;
	}

	public Member findByEmail(String email) {
		try {
			String sql = "SELECT m.id, m.name, m.email, m.password, m.role FROM member m WHERE m.email = ?";
			return this.jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, email);
		}
		catch (EmptyResultDataAccessException ex) {
			logger.warn("Not Found Member. email: {}, ", email, ex);
			return null;
		}
	}

	static {
		MEMBER_ROW_MAPPER = (resultSet, rowNum) -> {
			long id = resultSet.getLong("id");
			String name = resultSet.getString("name");
			String email = resultSet.getString("email");
			String password = resultSet.getString("password");
			String role = resultSet.getString("role");
			return Member.builder().id(id).name(name).email(email).password(password).role(role).build();
		};
	}

}
