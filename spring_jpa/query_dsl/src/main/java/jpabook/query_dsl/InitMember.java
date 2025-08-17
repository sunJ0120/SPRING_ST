package jpabook.query_dsl;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.query_dsl.entity.Member;
import jpabook.query_dsl.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/*
샘플 데이터 추가를 위한 클래스이다.
 */
@Profile("local")
@Component
@RequiredArgsConstructor
public class InitMember {
    private final InitMemberService initMemberService;

    @PostConstruct
    public void init() {
        initMemberService.init();
    }

    @Component
    static class InitMemberService {
        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");
            em.persist(teamA);
            em.persist(teamB);

            //페이징 등도 test 해야 하므로 많이 넣는다.
            for(int i = 0; i<100; i++) {
                Team selectedTeam = (i % 2 == 0) ? teamA : teamB; //짝수는 teamA, 홀수는 teamB
                em.persist(new Member("member" + i, i, selectedTeam));
            }
        }
    }
}
