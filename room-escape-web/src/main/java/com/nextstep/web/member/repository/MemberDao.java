package com.nextstep.web.member.repository;

import com.nextstep.web.member.repository.entity.MemberEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Optional;

@Component
public class MemberDao {
    public final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MemberEntity> rowMapper = (resultSet, rowNum) -> new MemberEntity(
            resultSet.getLong("id"),
            resultSet.getString("username"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("phone")
    );

    public Long save(MemberEntity memberEntity) {
        String sql = "INSERT INTO member (username, password, name, phone) VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, memberEntity.getUsername());
            ps.setString(2, memberEntity.getPassword());
            ps.setString(3, memberEntity.getName());
            ps.setString(4, memberEntity.getPhone());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Optional<MemberEntity> findById(Long id) {
        String sql = "SELECT id, username, password, name, phone from member where id = ?;";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
    }

    public Optional<MemberEntity> findByUsername(String username) {
        String sql = "SELECT id, username, password, name, phone from member where username = ?;";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, username));
    }
}
