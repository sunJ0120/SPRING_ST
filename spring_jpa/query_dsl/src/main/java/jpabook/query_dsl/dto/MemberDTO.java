package jpabook.query_dsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDTO {
    private String username;
    private int age;

    //Q클래스 생성을 위해서는 clean > rebuild 필요
    @QueryProjection
    public MemberDTO(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
