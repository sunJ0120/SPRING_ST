package hello.myJdbc.repository;

import hello.myJdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;
/*
JDBC - ConnectionParam
 */
@Slf4j
public class MemberRepositoryV2 {
    private final DataSource dataSource; //의존관계 주입
    public MemberRepositoryV2(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values(?, ?)";
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId()); //첫번째에 넣는다는 의미인가?
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e){
            log.error("db error", e);
            throw e;
        } finally {
            close(con,pstmt,null);
        }
    }

    public Member findById(String memberId) throws SQLException{
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();//실행하는것, 쿼리 결과를 반환

            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con,pstmt,rs);
        }
    }

    public Member findById(Connection con, String memberId) throws SQLException{
        String sql = "select * from member where member_id = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();//실행하는것, 쿼리 결과를 반환

            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            //connection을 close할지는 service가 결정해야 하기 때문에 여기서 닫으면 안된다.
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
        }
    }

    public void update(String memberId, int money) throws SQLException{
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize); //몇개 업데이트 되었는지 나옴.
        } catch (SQLException e){
            log.error("db error", e);
        } finally {
            close(con,pstmt, null);
        }
    }

    public void update(Connection con, String memberId, int money) throws SQLException{
        String sql = "update member set money=? where member_id=?";

        PreparedStatement pstmt = null;

        try{
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize); //몇개 업데이트 되었는지 나옴.
        } catch (SQLException e){
            log.error("db error", e);
        } finally {
            //마찬가지로, connection은 여기서 닫지 않음
            JdbcUtils.closeStatement(pstmt);
        }
    }

    public void delete(String memberId) throws SQLException{
        String sql = "delete from member where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            pstmt.executeUpdate(); //delete 수행
        } catch (SQLException e){
            log.error("db error", e);
        } finally {
            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs){
        JdbcUtils.closeResultSet(rs); //내부에 null처리 등이 다 되어 있음
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }
    //dataSource를 사용함으로써, test에 hikari....등 여러가지를 넣을 수 있다.
    private Connection getConnection() throws SQLException{
        Connection con = dataSource.getConnection(); //dataSource에서 커넥션 얻기

        log.info("get connection={}, class={}", con, con.getClass());
        return con; //dataSource에서 얻음
    }
}
