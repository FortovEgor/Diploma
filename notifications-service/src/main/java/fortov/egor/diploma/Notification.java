package fortov.egor.diploma;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
@EqualsAndHashCode(of = {"id"})
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String type;

    @Column(nullable = false, length = 254)
    private String content;

    @Column
    private LocalDateTime timeToShow;  // todo: phone format validation\

    @Column
    private Duration intervalToRepeat;

    @Column(nullable = false)
    private Long userId;

    @Column
    private Boolean immediately;
}
