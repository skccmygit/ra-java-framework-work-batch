package kr.co.skcc.base.bap.com.repository;

import kr.co.skcc.base.com.account.domain.hist.UserRoleHist;
import kr.co.skcc.base.com.account.domain.hist.pk.UserRoleHistPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleHistRepository extends JpaRepository<UserRoleHist, UserRoleHistPK> {

}
