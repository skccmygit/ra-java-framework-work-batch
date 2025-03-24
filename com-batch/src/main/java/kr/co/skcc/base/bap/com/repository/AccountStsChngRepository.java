package kr.co.skcc.base.bap.com.repository;

import kr.co.skcc.base.com.account.domain.hist.AccountStsChng;
import kr.co.skcc.base.com.account.domain.hist.pk.AccountStsChngPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountStsChngRepository extends JpaRepository<AccountStsChng, AccountStsChngPK> {
}
