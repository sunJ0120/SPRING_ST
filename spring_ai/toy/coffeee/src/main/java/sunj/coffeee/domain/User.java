package sunj.coffeee.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 고민...이거 auto_increment 안 쓰는게 더 좋다고 했는데 맞나
    private Long id;
    @Column(nullable = false)
    private String pwd;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String account; // TODO : 리팩터링 예정
    @Column(nullable = false)
    private String name;

    private String nickname;
    private String profile;
}
