package hello.myJdbc.service;

import hello.myJdbc.domain.Member;
import hello.myJdbc.repository.MemberRepositoryV1;
import hello.myJdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {
    private final MemberRepositoryV2 memberRepository;
    private final DataSource dataSource;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException{
        Connection con = dataSource.getConnection(); //여기서 connection을 생성한다.
        try {
            con.setAutoCommit(false);
            bizLogic(con, fromId, toId, money);
            con.commit();
        } catch (Exception e) { //예외로 중간에 끊길경우 롤백
            con.rollback();
            throw new IllegalStateException(e);
        } finally {
            //여기서 커넥션을 정리해야 한다.
            release(con);
        }

    }

    //자원 반납 메서드
    private static void release(Connection con) {
        if(con != null){
            try {
                con.setAutoCommit(true);
                con.close();
            } catch (Exception e) {
                log.info("error" + e);
            }
        }
    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(con,fromId); //con 넘겨줘야 같은 커넥션을 사용한다.
        Member toMember = memberRepository.findById(con,toId);

        memberRepository.update(con,fromId, fromMember.getMoney() - money);
        //오류케이스 임의로 생성
        validation(toMember);
        memberRepository.update(con,toId, toMember.getMoney() + money);
    }

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
