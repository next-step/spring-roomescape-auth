package roomescape.repository;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import roomescape.domain.ReservationTime;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ReservationTimeRepositoryTests {

	@InjectMocks
	private ReservationTimeRepository reservationTimeRepository;

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

		ReflectionTestUtils.setField(this.reservationTimeRepository, "jdbcInsert", this.jdbcInsert);
	}

	@Test
	void saveReservationTime() {
		// given
		ReservationTime reservationTime = ReservationTime.builder().id(1L).startAt("10:00").build();

		given(this.jdbcInsert.executeAndReturnKey(anyMap())).willReturn(1L);

		// when
		ReservationTime savedReservationTime = this.reservationTimeRepository.save(reservationTime);

		// then
		assertThat(savedReservationTime).isNotNull();
		assertThat(savedReservationTime.getId()).isEqualTo(1L);
	}

	@Test
	void deleteReservationTime() {
		// given
		long id = 1L;
		given(this.jdbcTemplate.update(anyString(), any(Object[].class))).willReturn(1);

		// when
		this.reservationTimeRepository.delete(id);

		// then
		verify(this.jdbcTemplate).update(anyString(), eq(id));
	}

	@Test
	void findReservationTimeById() {
		// given
		ReservationTime reservationTime = ReservationTime.builder().id(1L).startAt("10:00").build();

		given(this.jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), anyLong()))
			.willReturn(reservationTime);

		// when
		ReservationTime result = this.reservationTimeRepository.findById(1L);

		// then
		assertThat(result).isEqualTo(reservationTime);
	}

	@Test
	void findReservedTimeIds() {
		// given
		String date = "2024-06-18";
		long themeId = 1L;
		List<Long> ids = Arrays.asList(1L, 2L, 3L);

		given(this.jdbcTemplate.query(anyString(), any(RowMapper.class), any(Object[].class))).willReturn(ids);

		// when
		List<Long> reservedTimeIds = this.reservationTimeRepository.findReservedTimeIds(date, themeId);

		// then
		assertThat(reservedTimeIds).isNotNull();
		assertThat(reservedTimeIds).hasSize(ids.size());
		assertThat(reservedTimeIds).containsExactlyElementsOf(ids);
	}

}
