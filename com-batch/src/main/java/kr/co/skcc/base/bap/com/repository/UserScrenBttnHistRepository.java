package kr.co.skcc.base.bap.com.repository;

import kr.co.skcc.base.com.account.domain.hist.UserScrenBttnHist;
import kr.co.skcc.base.com.account.domain.hist.pk.UserScrenBttnHistPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserScrenBttnHistRepository extends JpaRepository<UserScrenBttnHist, UserScrenBttnHistPK> {

}
