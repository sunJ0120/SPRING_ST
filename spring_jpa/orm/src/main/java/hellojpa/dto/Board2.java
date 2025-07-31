package hellojpa.dto;

/*
테이블 전략 실습
 */

import jakarta.persistence.*;

@Entity
@TableGenerator(
        name = "BOARD2_SEQ_GENERATOR",
        table = "MY_SEQUENCES",
        pkColumnValue = "BOARD_SEQ",
        allocationSize = 1
)
public class Board2 {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,
    generator = "BOARD2_SEQ_GENERATOR")
    private Long id;
}
