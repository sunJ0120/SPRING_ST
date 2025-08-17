package jpabook.query_dsl.dto;

import lombok.Data;

/*
회원 검색을 위한 조건 클래스이다.
 */
@Data
public class MemberSearchCondition {
    //회원명, 팀명, 나이

    private String username; //회원명
    private String teamName; //팀명
    private Integer ageGoe; //나이 이상
    private Integer ageLoe; //나이 이하
}
