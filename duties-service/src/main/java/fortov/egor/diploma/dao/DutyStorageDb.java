package fortov.egor.diploma.dao;

import fortov.egor.diploma.Duty;
import fortov.egor.diploma.storage.DutyStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class DutyStorageDb implements DutyStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Duty save(Duty duty) {
        String sql = "INSERT INTO duties (start_time, interval, ids) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setObject(1, duty.getStart_time());
                    ps.setObject(2, duty.getInterval());
                    Array idsArray = connection.createArrayOf("bigint", duty.getIds());
                    ps.setArray(3, idsArray);
                    return ps;
                }, keyHolder);
        return duty;
    }

    @Override
    public Duty update(Duty duty) {
        Long id = duty.getId();
        String sql = "UPDATE duties SET start_time = ?, interval = ?, ids = ? WHERE id = ?";
        jdbcTemplate.update(sql, duty.getStart_time(), duty.getInterval(), duty.getIds(), id);
        return duty;
    }

    @Override
    public void delete(Long dutyId) {
        String sql = "DELETE FROM duties WHERE id = ?";
        jdbcTemplate.update(sql, dutyId);
    }

    @Override
    public List<Duty> getUserDuties(Long userId) {
        String sql = "SELECT id, start_time, interval, ids FROM duties WHERE ? = ANY(ids)";
        return jdbcTemplate.query(sql,
                (ResultSet rs, int rowNum) -> Duty.builder()
                        .id(rs.getLong(1))
                        .start_time(rs.getTimestamp(2).toLocalDateTime())
                        .interval((Duration) rs.getObject(3))
                        .ids((Long[]) rs.getObject(4))
                        .build(), userId);
    }

    @Override
    public List<Duty> getDutiesByIds(List<Long> dutiesIds) {
        String sql = "SELECT * FROM duties WHERE id IN ?";
        return jdbcTemplate.query(sql,
                (ResultSet rs, int rowNum) -> Duty.builder()
                        .id(rs.getLong(1))
                        .start_time(rs.getTimestamp(2).toLocalDateTime())
                        .interval((Duration) rs.getObject(3))
                        .ids((Long[]) rs.getObject(4))
                        .build(), dutiesIds);
    }
}
