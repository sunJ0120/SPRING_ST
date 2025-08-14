package jpabook.query_dsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jpabook.query_dsl.entity.Member;
import jpabook.query_dsl.entity.QMember;
import jpabook.query_dsl.entity.QTeam;
import jpabook.query_dsl.entity.Team;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static jpabook.query_dsl.entity.QMember.*;
import static jpabook.query_dsl.entity.QTeam.team;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @PersistenceContext
    EntityManager em;

    //getPersistenceUnitUtil()을 통해 fetch join을 확인할 수 있다.
    @PersistenceUnit
    EntityManagerFactory emf;

    // Querydsl을 사용하기 위해 JPAQueryFactory를 생성한다.
    // 이렇게 JPAQueryFactory를 필드로 빼는 것도 가능하다.
    JPAQueryFactory queryFactory;

    //@BeforeEach를 통해 테스트 객체를 초기화 한다.
    @BeforeEach
    public void before() {
        // EntityManager를 통해 JPAQueryFactory를 생성한다. @BeforeEach 안에서 생성해준다.
        queryFactory = new JPAQueryFactory(em);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    //JPQL로 실행해보기
    @Test
    public void startJPQL() {
        String qlString = "select m from Member m " +
                "where m.username = :username";

        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    //Querydsl로 실행해보기
    @Test
    public void startQuerydsl() {

        // Querydsl의 경우, 이렇게 method 짜듯이 쿼리를 작성할 수 있다.
        // 특히, 컴파일 시점에 알 수 있다는 장점이 있다.
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    //기본 검색 쿼리
    @Test
    public void search() {
        // Querydsl을 사용하여 member 엔티티를 검색
        // 검색 조건은 .and(), .or()을 통해 메서드 체인으로 연결할 수 있다.
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
        assertThat(findMember.getAge()).isEqualTo(10);
    }

    @Test
    public void searchAndParam() {
        List<Member> result1 = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        member.age.eq(10)
                ).fetch();

        assertThat(result1.size()).isEqualTo(1);
    }

    /**
     * 회원 정렬 순서
     *
     * 1. 회원 나이 내림차순 (desc)
     * 2. 회원 이름 올림차순 (asc)
     * 단, 2에서 회원 이름이 없으면 마지막에 출력 (nulls last)
     */
    @Test
    public void sort() throws Exception {
        //given
        //이 부분은, member
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        //when
        // Querydsl을 사용하여 member 엔티티를 정렬
        // 이걸 이용하면, member.username.asc() 별로 내림차순 정렬하되,
        // nullsLast()를 이용해서 null을 가장 마지막에 둘 수 있다.
        List<Member> result = queryFactory.
                selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        // get을 이용해서 몇번째~를 뽑는 것이다. 정렬이 잘 되었다면 뽑았을때 원하는 값이 나온다.
        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        //then
        //이걸 이용해서 result의 0번째가 member5가 맞는지 확인한다.
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    public void paging1() throws Exception {
        //given

        //when
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1) // 1번부터 시작
                .limit(2) // 2개만 가져오기
                .fetch();

        //then
        //.limit(2)로 몇개 까지 가져올 것인지 정할 수 있다.
        assertThat(result.size()).isEqualTo(2); // 2개가 나와야 한다.
    }

    //전체 조회수가 필요할 경우, 다음과 같이 처리한다.
    @Test
    public void paging2() throws Exception {
        //given

        //when
        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1) // 1번부터 시작
                .limit(2) // 2개만 가져오기
                .fetchResults();  //전체 조회수가 필요할 경우, 이렇게 처리하면 된다.

        //then
        assertThat(queryResults.getTotal()).isEqualTo(4); // 전체 조회수가 4개여야 한다.
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().size()).isEqualTo(2);
        //queryResults.getResults()의 경우는 contents가 나오는데, 이거 사이즈를 구하면 된다.
    }

    //집합 함수 테스트
    @Test
    public void aggregation() throws Exception {
        //given

        //when
        List<Tuple> result = queryFactory
                .select(
                        member.count(), // count
                        member.age.sum(), // sum
                        member.age.avg(), // avg
                        member.age.max(), // max
                        member.age.min() // min
                )
                .from(member)
                .fetch();

        Tuple tuple = result.get(0); //이걸로 한 줄의 결과를 가져오는 것이다.
        //then
        assertThat(tuple.get(member.count())).isEqualTo(4); // count
        assertThat(tuple.get(member.age.sum())).isEqualTo(100); // sum
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40); // max
        assertThat(tuple.get(member.age.min())).isEqualTo(10); // min
    }

    @Test
    public void group() throws Exception {
        //given
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg()) // team 이름과 age의 평균을 가져온다.
                .from(member)
                .join(member.team, team) // member와 team을 조인한다.
                .groupBy(member.team) // team별로 그룹핑한다.
                .fetch();
        //when
        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        //then
        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15); // teamA의 평균 나이
        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35); // teamB의 평균 나이
    }

    /*
    기본 조인

    팀 A에 소속된 모든 회원
    on절로 연관관계를 이용해서 조인한다.
     */
    @Test
    void join() throws Exception {
        //given
        QMember member = QMember.member;
        QTeam team = QTeam.team;

        //when
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team) // member와 team을 조인한다.
                .where(team.name.eq("teamA")) // teamA에 소속된 회원을 찾는다.
                .fetch();

        //then
        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2"); // teamA에 소속된 회원의 이름이 member1, member2여야 한다.
    }

    /*
    세타 조인

    연관관계가 없는 필드로 조인한다.
    세타 조인을 outer 조인으로 하기 위해서는 on절을 사용해야 한다.
     */
    @Test
    void theta_join() throws Exception {
        //given
        em.persist(new Member("teamA")); // teamA라는 이름을 가진 회원을 추가한다.
        em.persist(new Member("teamB"));

        //when
        List<Member> result = queryFactory
                .select(member)
                .from(member, team) // 세타 조인의 경우. from절에 두 개의 엔티티를 넣는다.
                .where(member.username.eq(team.name)) //모든 회원, 모든 팀을 가져온 다음에 조인을 하는 식으로 이뤄진다!
                .fetch();

        //then
        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB"); // teamA와 teamB라는 이름을 가진 회원이 나와야 한다.
    }

    /*
    조인 - on절 사용

    회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회한다.
     */
    @Test
    void join_on_filtering() throws Exception {
        //given
        //when
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team) // left join을 사용한다.
                .on(team.name.eq("teamA")) // teamA인 팀만 조인한다.
                .fetch();

        //then
        for(Tuple tuple : result){
            System.out.println("tuple = " + tuple);
        }
    }

    /*
    조인 - 연관관계 없는 필드로 조인

    회원의 이름과 팀의 이름이 같은 대상 외부 조인
     */
    @Test
    void join_on_no_relation() throws Exception {
        //given
        em.persist(new Member("teamA")); // teamA라는 이름을 가진 회원을 추가한다.
        em.persist(new Member("teamB"));

        //when
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name)) // left join을 사용하고, on절로 조인 조건을 설정한다.
                .fetch();

        //then
        for(Tuple tuple : result){
            System.out.println("tuple = " + tuple);
        }
    }

    /*
    조인 - 페치 조인 미적용
     */
    @Test
    void fetchJoinNo() throws Exception {
        //given
        em.flush();
        em.clear();

        //when
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        //then
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam()); // 팀이 로딩되었는지 확인
        assertThat(loaded).as("페치 조인 미적용").isFalse(); // 페치 조인이 적용되지 않았으므로 false여야 한다.
    }

    /*
    조인 - 페치 조인 적용

    즉시 로딩으로 한 번에 조회하기 때문에 isTrue() 값이 나온다.
     */
    @Test
    void fetchJoinUse() throws Exception {
        //given
        em.flush();
        em.clear();

        //when
        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin() // fetchJoin을 사용하여 팀을 즉시 로딩한다.
                .where(member.username.eq("member1"))
                .fetchOne();

        //then
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam()); // 팀이 로딩되었는지 확인
        assertThat(loaded).as("페치 조인 적용").isTrue(); // 페치 조인이 적용되지 않았으므로 false여야 한다.
    }

    /*
    서브 쿼리 - eq 사용

    나이가 가장 많은 회원 조회
     */
    @Test
    void subQuery() throws Exception {
        //given
        QMember memberSub = new QMember("memberSub");

        //when
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max()) // 서브 쿼리로 나이가 가장 많은 회원의 나이를 조회
                                .from(memberSub)
                ))
                .fetch();

        //then
        assertThat(result).extracting("age")
                .containsExactly(40); // 나이가 40인 회원이 나와야 한다.
    }

    /*
    서브 쿼리 - goe 사용

    나이가 평균 나이 이상인 회원을 뽑는다.
     */
    @Test
    void subQueryGoe() throws Exception {
        //given
        QMember memberSub = new QMember("memberSub");

        //when
        //한마디로, 평균나이보다 위여야 한다는 의미이다.
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        select(memberSub.age.avg()) // 서브 쿼리로 평균 나이를 조회
                                .from(memberSub)
                ))
                .fetch();

        //then
        assertThat(result).extracting("age")
                .containsExactly(30, 40); // 나이가 30 이상인 회원이 나와야 한다.

    }

    /*
    서브쿼리 - in 사용

    여러건을 처리한다.
     */
    @Test
    void subQueryIn() throws Exception {
        //given
        QMember memberSub = new QMember("memberSub");

        //when
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        select(memberSub.age) // 서브 쿼리로 나이 조회
                                .from(memberSub)
                                .where(memberSub.age.gt(10)) // 나이가 10보다 큰 회원을 조회
                ))
                .fetch();

        //then
        assertThat(result).extracting("age")
                .containsExactly(20, 30, 40); // 나이가 20, 30, 40인 회원이 나와야 한다.
    }

    /*
    case문 - 단순한 조건
     */
    @Test
    void case_1() throws Exception {
        //given

        //when
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타")) // 나이에 따라 다른 문자열을 반환
                .from(member)
                .fetch();

        //then
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    /*
    case문 - 복잡한 조건

    between을 사용하여 나이에 따라 다른 문자열을 반환한다.
     */
    @Test
    void case_2() throws Exception {
        //given

        //when
        List<String> result = queryFactory
                .select(new CaseBuilder()
                    .when(member.age.between(0, 20)).then("0~20살")
                    .when(member.age.between(21, 30)).then("21~30살")
                    .otherwise("기타")) // 나이에 따라 다른 문자열을 반환
                .from(member)
                .fetch();

        //then
        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    /*
    case문 - 복잡한 조건과 정렬

    우선순위에 따라 출력하는 방식을 알아보자.
    1. 0~30살이 아닌 회원을 가장 먼저 출력한다.
    2. 0~20살 회원을 출력한다.
    3. 21~30살 회원을 출력한다.
     */
    @Test
    void case_3() throws Exception {
        //given

        //when
        NumberExpression<Integer> rankPath = new CaseBuilder()
                .when(member.age.between(0, 20)).then(2)
                .when(member.age.between(21, 30)).then(1)
                .otherwise(3);

        List<Tuple> result = queryFactory
                .select(member.username, member.age, rankPath) // 회원 이름과 rankPath를 선택
                .from(member)
                .orderBy(rankPath.desc()) // rankPath를 기준으로 내림차순 정렬
                .fetch();

        //then
        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            Integer rank = tuple.get(rankPath);
            System.out.println("username = " + username + ", age = " + age + ", rank = " + rank);
        }
    }

    /*
    상수, 문자 더하기
     */
    @Test
    public void 상수더하기() throws Exception {
        //given

        //when
        Tuple result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetchFirst();

        //then
        System.out.println("result = " + result);
    }

    @Test
    public void 문자더하기() throws Exception {
        //given

        //when
        String result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        //then
        System.out.println("result = " + result); // member1_10이 출력되어야 한다.

    }
}
