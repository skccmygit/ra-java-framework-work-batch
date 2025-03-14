package kr.co.skcc.oss.bap.com.repository;

import jakarta.persistence.QueryHint;
import kr.co.skcc.oss.com.account.domain.userSpecificMenu.MyViewDtl;
import kr.co.skcc.oss.com.account.domain.userSpecificMenu.pk.MyViewDtlPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface MyViewDtlRepository extends JpaRepository<MyViewDtl, MyViewDtlPK> {

    @QueryHints(@QueryHint(name=COMMENT, value="공통, common, 천지은"))
    @Modifying
    @Query(value = "DELETE FROM MyViewDtl t \n" +
                   " WHERE t.userScrenCnstteSeq IN ( SELECT k.userScrenCnstteSeq \n" +
                   "                                  FROM MyView k \n" +
                   "                                 WHERE k.userid = :userid ) \n")
    void deleteMyViewDtl(@Param("userid") String userid);
}
