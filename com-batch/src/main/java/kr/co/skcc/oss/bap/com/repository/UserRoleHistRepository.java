package kr.co.skcc.oss.bap.com.repository;

import kr.co.skcc.oss.com.account.domain.hist.UserRoleHist;
import kr.co.skcc.oss.com.account.domain.hist.pk.UserRoleHistPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleHistRepository extends JpaRepository<UserRoleHist, UserRoleHistPK> {

}
