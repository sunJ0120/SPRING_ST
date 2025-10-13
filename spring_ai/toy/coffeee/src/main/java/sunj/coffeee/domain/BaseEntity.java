package sunj.coffeee.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass //TODO : 이건 왜 쓰는거
@EntityListeners(AuditingEntityListener.class) //TODO : 이건 왜 쓰는거
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {
    @CreatedDate
    @Column(updatable = false) //TODO : 이건 왜 쓰는거
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
