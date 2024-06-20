package roomescape.reservationtime.domain;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.domain.entity.ReservationTime;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationTimeJdbcTemplateRepository implements ReservationTimeRepository {
    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReservationTime> rowMapper = (resultSet, rowNum) -> {
        return ReservationTime.of(
                resultSet.getLong("id"),
                resultSet.getString("start_at")
        );
    };
    @Override
    public List<ReservationTime> findAll() {
        String sql = """
                    select *
                    from reservation_time
                    order by start_at
                    """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<ReservationTime> findMatchWith(String date, Long themeId) {
        String sql = """
                select *
                from reservation_time as t
                left join reservation as r
                    on t.id = r.time_id
                    and r.date = ?
                    and r.theme_id = ?
                where r.time_id is null
                order by start_at
                """;
        return jdbcTemplate.query(sql, rowMapper, date, themeId);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = """
                    select * from
                    reservation_time
                    where id = ?
                    """;

        try {
            ReservationTime reservationTime = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(reservationTime);
        }
        catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ReservationTime> findByStartAt(String startAt) {
        String sql = """
                    select *
                    from reservation_time
                    where start_at = ?
                    """;

        try {
            ReservationTime reservationTime = jdbcTemplate.queryForObject(sql, rowMapper, startAt);
            return Optional.ofNullable(reservationTime);
        }
        catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Long countReservationMatchWith(Long id) {
        String sql = """
                    select count(*)
                    from reservation as r
                    inner join reservation_time as t
                        on r.time_id = t.id
                    where t.id = ?
                    """;
        return jdbcTemplate.queryForObject(sql, Long.class, id);
    }

    @Override
    public long save(String startAt) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connect -> {
            String sql = """
                        insert into reservation_time
                        (start_at)
                        values (?)
                        """;
            PreparedStatement ps = connect.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, startAt);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public long deleteById(Long id) {
        String sql = """
                    delete from reservation_time
                    where id = ?
                    """;
        return jdbcTemplate.update(sql, id);
    }
}
