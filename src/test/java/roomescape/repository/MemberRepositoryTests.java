package roomescape.repository;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.support.PasswordEncoder;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class MemberRepositoryTests {

	@InjectMocks
	private MemberRepository memberRepository;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private SimpleJdbcInsert jdbcInsert;

	@Mock
	private DataSource dataSource;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		given(this.jdbcTemplate.getDataSource()).willReturn(this.dataSource);

		this.jdbcInsert = mock(SimpleJdbcInsert.class);

		ReflectionTestUtils.setField(this.memberRepository, "jdbcInsert", this.jdbcInsert);
	}

	@Test
	void isExistsWhenMemberExists() {
		// given
		given(this.jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyString())).willReturn(1);

		// when
		boolean exists = this.memberRepository.isExists("tester");

		// then
		assertThat(exists).isTrue();
	}

	@Test
	void isExistsWhenMemberDoesNotExist() {
		// given
		given(this.jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyString())).willReturn(null);

		// when
		boolean exists = this.memberRepository.isExists("tester");

		// then
		assertThat(exists).isFalse();
	}

	@Test
	void save() {
		// given
		Member member = Member.builder()
			.name("tester")
			.email("tester@gmail.com")
			.password(PasswordEncoder.encode("1234"))
			.role(MemberRole.USER.name())
			.build();

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("name", member.getName());
		parameters.put("email", member.getEmail());
		parameters.put("password", member.getPassword());
		parameters.put("role", MemberRole.USER.name());

		given(this.jdbcInsert.executeAndReturnKey(parameters)).willReturn(1L);

		// when
		Member savedMember = this.memberRepository.save(member);

		// then
		assertThat(savedMember.getId()).isEqualTo(1L);
	}

	@Test
	void findByEmailWhenMemberExists() {
		// given
		String email = "tester@gmail.com";
		Member member = Member.builder()
			.id(1L)
			.name("tester")
			.email(email)
			.password("encodedPassword")
			.role(MemberRole.USER.name())
			.build();

		given(this.jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyString())).willReturn(member);

		// when
		Member foundMember = this.memberRepository.findByEmail(email);

		// then
		assertThat(foundMember).isEqualTo(member);
	}

	@Test
	void findByEmailWhenMemberDoesNotExist() {
		// given
		String email = "tester@gmail.com";
		given(this.jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyString())).willReturn(null);

		// when
		Member foundMember = this.memberRepository.findByEmail(email);

		// then
		assertThat(foundMember).isNull();
	}

}
