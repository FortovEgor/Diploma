package fortov.egor.diploma;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.h2.api.Interval;
import org.hibernate.annotations.Type;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
//@Entity
//@Table(name = "notifications")
//@EqualsAndHashCode(of = {"id"})
@Builder
public class Notification {
    private Long id;
    private String type;

//    @Column(nullable = false, length = 254)
    private String content;

//    @Column
    private LocalDateTime time_to_show;  // todo: phone format validation\

//    @Column
//    @Type(PostgreSQLIntervalType.class)
    private Duration interval_to_repeat;

//    @Column(nullable = false)
    private Long userId;

//    @Column
    private Boolean immediately;
}
