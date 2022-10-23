package com.nextstep.web.reservation.repository;

import com.nextstep.web.reservation.repository.entity.ReservationEntity;
import com.nextstep.web.schedule.repository.ScheduleDao;
import com.nextstep.web.theme.repository.ThemeDao;
import com.nextstep.web.theme.repository.entity.ThemeEntity;
import nextstep.common.BusinessException;
import nextstep.domain.reservation.Reservation;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class ReservationDao {
    private static final String TABLE_NAME = "Reservation";
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final ReservationRowMapper rowMapper;
    private final ScheduleDao scheduleDao;

    public ReservationDao(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource, ScheduleDao scheduleDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.scheduleDao = scheduleDao;
        this.rowMapper = new ReservationRowMapper();
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME);

    }

    public Long save(ReservationEntity reservationEntity) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("scheduleId", reservationEntity.getScheduleEntity());
        parameters.put("reservationTime", reservationEntity.getReservationTime());
        parameters.put("name", reservationEntity.getName());

        return (long) jdbcInsert.execute(parameters);
    }

    public List<ReservationEntity> findByMemberName(String name) {
        String query = "SELECT * FROM RESERVATION WHERE name = :name";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("name", name);
        return jdbcTemplate.query(query, namedParameters, rowMapper);
    }

    public List<ReservationEntity> findAllBy(List<Long> scheduleIds) {
        if (scheduleIds.isEmpty()) {
            return Collections.emptyList();
        }

        String query = "SELECT * FROM RESERVATION WHERE scheduleId IN :scheduleIds";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("scheduleId", scheduleIds);

        return jdbcTemplate.query(query, namedParameters, rowMapper);
    }

    public void delete(Long id) {
        String query = "DELETE FROM RESERVATION WHERE id = :id AND time = :time";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", id);
        jdbcTemplate.update(query, namedParameters);
    }

    public Optional<ReservationEntity> findById(Long id) {
        String query = "SELECT * FROM RESERVATION WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", id);
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, namedParameters, rowMapper));
    }

    public class ReservationRowMapper implements RowMapper<ReservationEntity> {
        @Override
        public ReservationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

            ReservationEntity reservationEntity = new ReservationEntity(
                    rs.getLong("ID"),
                    scheduleDao.findById(rs.getLong("scheduleId"))
                            .orElseThrow(() -> new BusinessException("")),
                    rs.getObject("reservationTime", LocalDateTime.class),
                    rs.getString("name")
            );
            return reservationEntity;
        }
    }
}
