package fortov.egor.diploma.dao;

import fortov.egor.diploma.Duty;
import fortov.egor.diploma.exception.DBException;
import fortov.egor.diploma.storage.DutyStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.PreparedStatement;
import org.postgresql.jdbc.PgArray;
import java.sql.ResultSet;
import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class DutyStorageDb implements DutyStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Duty save(Duty duty) {
        String sql = "INSERT INTO duties (name, start_time, interval, ids) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(
                    connection -> {
                        PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                        ps.setString(1, duty.getName());
                        ps.setObject(2, duty.getStart_time());
                        ps.setLong(3, duty.getInterval().toSeconds());
                        Array idsArray = connection.createArrayOf("bigint", duty.getIds());
                        ps.setArray(4, idsArray);
                        return ps;
                    }, keyHolder);
        } catch (Exception e) {
            throw new DBException();
        }

        duty.setId(keyHolder.getKey().longValue());
        return duty;
    }

    @Override
    public Duty update(Duty duty) {
        Long id = duty.getId();
        String sql = "UPDATE duties SET name = ?, start_time = ?, interval = ?, ids = ? WHERE id = ?";
        try {
            jdbcTemplate.update(sql, duty.getName(), duty.getStart_time(), duty.getInterval().toSeconds(), duty.getIds(), id);
        } catch (Exception e) {
            throw new DBException();
        }

        return duty;
    }

    @Override
    public void delete(Long dutyId) {
        String sql = "DELETE FROM duties WHERE id = ?";
        try {
            jdbcTemplate.update(sql, dutyId);
        } catch (Exception e) {
            throw new DBException();
        }
    }

    @Override
    public List<Duty> getUserDuties(Long userId) {
        String sql = "SELECT id, name, start_time, interval, ids FROM duties WHERE ? = ANY(ids)";

        List<Duty> duties;
        try {
            duties = jdbcTemplate.query(sql,
                    (ResultSet rs, int rowNum) -> {
                        Long[] ids = (Long[]) ((PgArray) rs.getArray("ids")).getArray();
                        return Duty.builder()
                                .id(rs.getLong(1))
                                .name(rs.getString(2))
                                .start_time(rs.getTimestamp(3).toLocalDateTime())
                                .interval(Duration.ofSeconds(rs.getLong(4)))
                                .ids(ids)
                                .build();
                    }, userId);
        } catch (Exception e) {
            throw new DBException();
        }

        return duties;
    }

    @Override
    public List<Duty> getAllDuties() {
        String sql = "SELECT id, name, start_time, interval, ids FROM duties";

        List<Duty> duties;
        try {
            duties = jdbcTemplate.query(sql,
                    (ResultSet rs, int rowNum) -> {
                        Long[] ids = (Long[]) ((PgArray) rs.getArray("ids")).getArray();
                        return Duty.builder()
                                .id(rs.getLong(1))
                                .name(rs.getString(2))
                                .start_time(rs.getTimestamp(3).toLocalDateTime())
                                .interval(Duration.ofSeconds(rs.getLong(4)))
                                .ids(ids)
                                .build();
                    });
        } catch (Exception e) {
            throw new DBException();
        }

        return duties;
    }

    @Override
    public List<Duty> getDutiesByIds(List<Long> dutiesIds) {
        String sql = "SELECT * FROM duties WHERE id IN ?";
        List<Duty> duties;
        try {
            duties = jdbcTemplate.query(sql,
                    (ResultSet rs, int rowNum) -> {
                        Long[] ids = (Long[]) ((PgArray) rs.getArray("ids")).getArray();
                        return Duty.builder()
                                .id(rs.getLong(1))
                                .name(rs.getString(2))
                                .start_time(rs.getTimestamp(3).toLocalDateTime())
                                .interval(Duration.ofSeconds(rs.getLong(4)))
                                .ids(ids)
                                .build();
                    }, dutiesIds);
        } catch (Exception e) {
            throw new DBException();
        }

        return duties;
    }

    @Override
    public Duty getDutyById(Long dutyId) {
        String sql = "SELECT * FROM duties WHERE id = ? LIMIT 1";
        List<Duty> duties;

        try {
            duties = jdbcTemplate.query(sql,
                    (ResultSet rs, int rowNum) -> {
                        Long[] ids = (Long[]) ((PgArray) rs.getArray("ids")).getArray();
                        return Duty.builder()
                                .id(rs.getLong(1))
                                .name(rs.getString(2))
                                .start_time(rs.getTimestamp(3).toLocalDateTime())
                                .interval(Duration.ofSeconds(rs.getLong(4)))
                                .ids(ids)
                                .build();
                    }, dutyId);
        } catch (Exception e) {
            throw new DBException();
        }

        if (duties.isEmpty()) return null;
        return duties.getFirst();
    }
}
