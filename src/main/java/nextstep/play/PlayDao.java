package nextstep.play;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class PlayDao {

    public final JdbcTemplate jdbcTemplate;

    public PlayDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Play> rowMapper = (resultSet, rowNum) -> new Play(
        resultSet.getLong("id"),
        resultSet.getLong("reservation_id"),
        resultSet.getLong("member_id"),
        resultSet.getBoolean("hidden")
    );

    public Long save(Play play) {
        String sql = "INSERT INTO play (reservation_id, member_id, hidden) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, play.getReservationId());
            ps.setLong(2, play.getMemberId());
            ps.setBoolean(3, play.isHidden());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void updateHidden(Long id, boolean hidden) {
        jdbcTemplate.update("UPDATE play SET hidden = ? WHERE id = ?", hidden, id);
    }

    public List<Play> findAllHiddenPlaysByMemberId(Long memberId) {
        String sql = "SELECT id, reservation_id, member_id, hidden FROM play WHERE member_id = ? AND hidden = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId, false);
    }
}
