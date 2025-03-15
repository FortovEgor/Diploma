package fortov.egor.diploma.dao;

import fortov.egor.diploma.Notification;
import fortov.egor.diploma.storage.NotificationsStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.util.List;

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
                    ps.setString(1, notification.getType());
                    ps.setString(2, notification.getContent());
                    ps.setObject(3, notification.getTime_to_show());
                    ps.setLong(4, notification.getInterval_to_repeat().toSeconds());
                    ps.setLong(5, notification.getUserId());
                    ps.setBoolean(6, notification.getImmediately());
                    return ps;
                }, keyHolder);
        notification.setId(keyHolder.getKey().longValue());
        return notification;
    }

    @Override
    public void deleteById(Long NotificationId) {
        String sql = "DELETE FROM notifications WHERE id = ?";
        jdbcTemplate.update(sql, NotificationId);
    }

    @Override
    public void deleteAllById(List<Long> notificationsIds) {
        String sql = "DELETE FROM notifications WHERE id IN ?";
        jdbcTemplate.update(sql, notificationsIds);
    }

    @Override
    public Notification findById(Long notificationId) {
        String sql = "SELECT id, type, content, time_to_show, interval_to_repeat, user_id, immediately " +
                "FROM notifications WHERE id = ? LIMIT 1";
        List<Notification> notifications = jdbcTemplate.query(sql,
                            (ResultSet rs, int rowNum) -> Notification.builder()
                                    .id(rs.getLong("id"))
                                    .type(rs.getString("type"))
                                    .content(rs.getString("content"))
                                    .time_to_show(rs.getTimestamp("time_to_show").toLocalDateTime())
                                    .interval_to_repeat(Duration.ofSeconds(rs.getLong("interval_to_repeat")))
                                    .userId(rs.getLong("user_id"))
                                    .immediately(rs.getBoolean("immediately"))
                                    .build(), notificationId);
        if (notifications.isEmpty()) return null;
        return notifications.getFirst();
    }

    @Override
    public List<Notification> findAllById(List<Long> notificationsIds) {
        String sql = "SELECT id, type, content, time_to_show, interval_to_repeat, user_id, immediately " +
                "FROM notifications WHERE id = ANY(?)";
        Long[] idsArray = notificationsIds.toArray(new Long[0]);
        return jdbcTemplate.query(sql,
                (ResultSet rs, int rowNum) -> Notification.builder()
                        .id(rs.getLong("id"))
                        .type(rs.getString("type"))
                        .content(rs.getString("content"))
                        .time_to_show(rs.getTimestamp("time_to_show").toLocalDateTime())
                        .interval_to_repeat(Duration.ofSeconds(rs.getLong("interval_to_repeat")))
                        .userId(rs.getLong("user_id"))
                        .immediately(rs.getBoolean("immediately"))
                        .build(), new Object[]{idsArray});
    }
}
