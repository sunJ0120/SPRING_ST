package hello.myJdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.myJdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static hello.myJdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV1Test {
    MemberRepositoryV1 repository;
    @BeforeEach //실행 전에 시작한다.
    void beforeEach() throws Exception{
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL); //비번 등 세팅
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        repository = new MemberRepositoryV1(dataSource); //생성자 주입을 통해 dataSource 주입
    }

    @Test
    void crud() throws SQLException, InterruptedException{
        log.info("start");
        //save
        Member member = new Member("memberV0", 10000);
        repository.save(member);

        //findById
        Member findMember = repository.findById(member.getMemberId());
        assertThat(findMember).isNotNull();

        //update : money : 10000-> 20000
        repository.update(member.getMemberId(), 20000);
        Member updatedMember = repository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        //delete
        repository.delete(member.getMemberId());
        //new NoSuchElementException
        assertThatThrownBy(() -> repository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class); //NoSuchElementException.class 이거에 속하는지를 본다.
    }
}