package kr.co.skcc.base.bap.com.repository;

import kr.co.skcc.base.com.account.domain.hist.UserMenuHist;
import kr.co.skcc.base.com.account.domain.hist.pk.UserMenuHistPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMenuHistRepository extends JpaRepository<UserMenuHist, UserMenuHistPK> {
}
