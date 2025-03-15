package fortov.egor.diploma.user;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@ToString
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 250)
    private String name;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column
    private String phone;

    @Column
    private String password;

    @Column
    private String organization;

    @Column
    private String avatar;

    @Column
    private String about;
}
