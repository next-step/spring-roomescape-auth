package roomescape.adapter.out.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.adapter.mapper.ReservationMapper;
import roomescape.adapter.out.ReservationEntity;
import roomescape.adapter.out.ReservationTimeEntity;
import roomescape.application.port.out.ReservationPort;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Primary
@Repository
public class ReservationJdbcRepository implements ReservationPort {

  private final JdbcTemplate jdbcTemplate;

  public ReservationJdbcRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Reservation> findReservations() {
    String sql =
      "select reservation.id, reservation.name, reservation.date, reservation.time_id, reservation_time.start_at from reservation INNER JOIN reservation_time on reservation.time_id = reservation_time.id";

    List<ReservationEntity> reservationEntities = jdbcTemplate.query(
      sql, (resultSet, rowNum) -> {
        Long id = resultSet.getLong("time_id");
        String time = resultSet.getString("start_at");

        ReservationTimeEntity reservationTimeEntity = ReservationTimeEntity.of(id, time);

        ReservationEntity reservationEntity = new ReservationEntity(
          resultSet.getLong("id"),
          resultSet.getString("name"),
          resultSet.getString("date"),
          reservationTimeEntity,
          null
        );

        return reservationEntity;
      }
    );

    return reservationEntities.stream()
                              .map(ReservationMapper::mapToDomain)
                              .toList();

  }

  @Override
  public Optional<Reservation> findReservationByReservationTime(ReservationTime reservationTime) {
    String sql =
      "select reservation.id, reservation.name, reservation.date, reservation.time_id, reservation_time.start_at from reservation INNER JOIN reservation_time on reservation.time_id = reservation_time.id WHERE reservation_time.start_at = ?";

    List<ReservationEntity> reservationEntities = jdbcTemplate.query(
      sql, (resultSet, rowNum) -> {
        Long id = resultSet.getLong("time_id");
        String time = resultSet.getString("start_at");

        ReservationTimeEntity reservationTimeEntity = ReservationTimeEntity.of(id, time);

        ReservationEntity reservationEntity = new ReservationEntity(
          resultSet.getLong("id"),
          resultSet.getString("name"),
          resultSet.getString("date"),
          reservationTimeEntity,
          null
        );

        return reservationEntity;
      }, reservationTime.getStartAt()
    );

    Reservation reservation = null;

    if (!reservationEntities.isEmpty()) {
      reservation = ReservationMapper.mapToDomain(reservationEntities.get(0));
    }

    return Optional.ofNullable(reservation);
  }

  @Override
  public Reservation saveReservation(Reservation reservation) {
    SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
      .withTableName("reservation")
      .usingGeneratedKeyColumns("id");

    Map<String, Object> columnMap = new HashMap<>();
    columnMap.put("name", reservation.getName());
    columnMap.put("date", reservation.getDate());
    columnMap.put("time_id", reservation.getTime().getId());
    columnMap.put("theme_id", reservation.getTheme().getId());

    Number reservationId = jdbcInsert.executeAndReturnKey(columnMap);

    return reservation.addId(reservationId.longValue());
  }

  @Override
  public void deleteReservation(Long id) {
    String sql = "DELETE FROM reservation WHERE id = ?";

    jdbcTemplate.update(sql, id);
  }

  @Override
  public Integer countReservationById(Long id) {
    String sql = "SELECT COUNT(*) FROM reservation WHERE id = ?";

    return jdbcTemplate.queryForObject(sql, Integer.class, id);
  }
}
