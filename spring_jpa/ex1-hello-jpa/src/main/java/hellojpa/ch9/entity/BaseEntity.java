package hellojpa.ch9.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {
    private Date createdDate; // 생성일자
    private Date lastModifiedDate; // 수정일자
}
