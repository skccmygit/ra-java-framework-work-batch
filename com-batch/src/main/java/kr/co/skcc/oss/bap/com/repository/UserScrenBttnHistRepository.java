package kr.co.skcc.oss.bap.com.repository;

import jakarta.persistence.QueryHint;
import kr.co.skcc.oss.com.account.domain.hist.UserScrenBttnHist;
import kr.co.skcc.oss.com.account.domain.hist.pk.UserScrenBttnHistPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface UserScrenBttnHistRepository extends JpaRepository<UserScrenBttnHist, UserScrenBttnHistPK> {

}
