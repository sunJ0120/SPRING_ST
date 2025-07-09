package hellojpa.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "MEMBER", uniqueConstraints = {@UniqueConstraint(
        name = "NAME_AGE_UNIQUE", columnNames = {"NAME", "AGE"})})
public class Member {
    @Id
    @Column(name="ID")
    private String id;

    @Column(name="NAME", nullable = false, length = 10) //추가
    private String username;
    private Integer age;

    //요구사항 추가에 따라서 추가된 부분이다.
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp lastModifiedDate;

    @Lob
    private String description;
}
