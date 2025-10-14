package sunj.test_practice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Study {
    private StudyStatus status = StudyStatus.DRAFT; //test를 위한 init
    private int limit;

    // ThreadLocal test
    ThreadLocal<Study> threadLocal = new ThreadLocal<>();

    public Study(int limit){
        // test용
        if(limit < 0){
            throw new IllegalArgumentException("limit는 0보다 커야 한다.");
        }
        this.limit = limit;
    }
}
