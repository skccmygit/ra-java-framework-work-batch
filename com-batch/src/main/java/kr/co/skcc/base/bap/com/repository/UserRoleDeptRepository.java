package kr.co.skcc.base.bap.com.repository;

import kr.co.skcc.base.com.account.domain.baseAuth.UserRoleDept;
import kr.co.skcc.base.com.account.domain.baseAuth.pk.UserRoleDeptPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleDeptRepository extends JpaRepository<UserRoleDept, UserRoleDeptPK> {

}
