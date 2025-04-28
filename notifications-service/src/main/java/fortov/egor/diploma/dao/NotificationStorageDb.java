package fortov.egor.diploma.dao;

import fortov.egor.diploma.Notification;
import fortov.egor.diploma.exception.ConflictException;
import fortov.egor.diploma.exception.DBException;
import fortov.egor.diploma.storage.NotificationsStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class NotificationStorageDb implements NotificationsStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Notification save(Notification notification) {
        String sql = "INSERT INTO notifications (type, content, time_to_show, interval_to_repeat, user_id, immediately) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                    setNullable(ps, 1, notification.getType(), Types.VARCHAR);
                    setNullable(ps, 2, notification.getContent(), Types.VARCHAR);
                    setNullable(ps, 3, notification.getTime_to_show() == null ? null : Timestamp.valueOf(notification.getTime_to_show()), Types.TIMESTAMP);
                    setNullable(ps, 4, notification.getInterval_to_repeat() != null ?
                            Long.valueOf(notification.getInterval_to_repeat().toSeconds()) : null, Types.BIGINT);
                    setNullable(ps, 5, notification.getUserId(), Types.BIGINT);
                    setNullable(ps, 6, notification.getImmediately(), Types.BOOLEAN);
                    return ps;
                },
                keyHolder
        );

        notification.setId(keyHolder.getKey().longValue());
        return notification;
    }

    @Override
    public Notification update(Notification notification) {
        String sql =
                "UPDATE notifications SET type = ?, content = ?, time_to_show = ?, interval_to_repeat = ?, immediately = ? WHERE id = ?";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            setNullable(ps, 1, notification.getType(), Types.VARCHAR);
            setNullable(ps, 2, notification.getContent(), Types.VARCHAR);
            setNullable(ps, 3, notification.getTime_to_show() == null ? null : Timestamp.valueOf(notification.getTime_to_show()), Types.TIMESTAMP);
            setNullable(ps, 4, notification.getInterval_to_repeat() != null ? Long.valueOf(notification.getInterval_to_repeat().toSeconds()) : null, Types.BIGINT);
            setNullable(ps, 5, notification.getImmediately(), Types.BOOLEAN);
            ps.setLong(6, notification.getId());

            return ps;
        });

        return notification;
    }

    @Override
    public void deleteById(Long NotificationId) {
        String sql = "DELETE FROM notifications WHERE id = ?";
        try {
            jdbcTemplate.update(sql, NotificationId);
        } catch (Exception e) {
            log.error("Error with DB: {}", e.getMessage());
            throw new DBException();
        }
    }

    @Override
    public void deleteAllById(List<Long> notificationsIds) {
        if (notificationsIds == null || notificationsIds.isEmpty()) {
            return;
        }

        String sql = "DELETE FROM notifications WHERE id IN (" +
                String.join(",", Collections.nCopies(notificationsIds.size(), "?")) + ")";

        try {
            jdbcTemplate.update(sql, notificationsIds.toArray());
        } catch (Exception e) {
            log.error("Error with DB: {}", e.getMessage());
            throw new DBException();
        }
    }

    @Override
    public Notification findById(Long notificationId) {
        String sql = "SELECT id, type, content, time_to_show, interval_to_repeat, user_id, immediately " +
                "FROM notifications WHERE id = ? LIMIT 1";
        List<Notification> notifications;
        try {
            notifications = jdbcTemplate.query(sql,
                    (ResultSet rs, int rowNum) -> Notification.builder()
                            .id(rs.getLong("id"))
                            .type(rs.getString("type"))
                            .content(rs.getString("content"))
                            .time_to_show(rs.getTimestamp("time_to_show") == null ? null :
                                    rs.getTimestamp("time_to_show").toLocalDateTime())
                            .interval_to_repeat(rs.getObject("interval_to_repeat") == null ? null :
                                    Duration.ofSeconds(rs.getLong("interval_to_repeat")))
                            .userId(rs.getLong("user_id"))
                            .immediately(rs.getBoolean("immediately"))
                            .build(), notificationId);
        } catch (Exception e) {
            log.error("Error with DB: {}", e.getMessage());
            throw new DBException();
        }

        if (notifications.isEmpty()) return null;
        return notifications.getFirst();
    }

    @Override
    public List<Notification> findAllById(List<Long> notificationsIds) {
        String sql = "SELECT id, type, content, time_to_show, interval_to_repeat, user_id, immediately " +
                "FROM notifications WHERE id = ANY(?)";
        Long[] idsArray = notificationsIds.toArray(new Long[0]);

        List<Notification> notiofications;
        try {
            notiofications = jdbcTemplate.query(sql,
                    (ResultSet rs, int rowNum) -> Notification.builder()
                            .id(rs.getLong("id"))
                            .type(rs.getString("type"))
                            .content(rs.getString("content"))
                            .time_to_show(rs.getTimestamp("time_to_show") == null ? null :
                                    rs.getTimestamp("time_to_show").toLocalDateTime())
                            .interval_to_repeat(rs.getObject("interval_to_repeat") == null ? null :
                                    Duration.ofSeconds(rs.getLong("interval_to_repeat")))
                            .userId(rs.getLong("user_id"))
                            .immediately(rs.getBoolean("immediately"))
                            .build(), new Object[]{idsArray});
        } catch (Exception e) {
            log.error("Error with DB: {}", e.getMessage());
            throw new DBException();
        }

        return notiofications;
    }

    // Вспомогательный метод для обработки NULL-значений
    private void setNullable(PreparedStatement statement, int index, Object value, int sqlType) throws SQLException {
        if  (value == null) {
            statement.setNull(index, sqlType);
        } else {
            switch (sqlType) {
                case Types.VARCHAR:
                    statement.setString(index, (String)value);
                    break;
                case Types.TIMESTAMP:
                    statement.setTimestamp(index, (Timestamp)value);
                    break;
                case Types.BIGINT:
                    statement.setLong(index, (Long)value);
                    break;
                case Types.BOOLEAN:
                    statement.setBoolean(index, (Boolean)value);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported SQL type: " + sqlType);
            }
        }
    }
}
