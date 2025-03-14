package kr.co.skcc.oss.bap.com.repository;

import kr.co.skcc.oss.com.account.domain.baseAuth.UserRoleDept;
import kr.co.skcc.oss.com.account.domain.baseAuth.pk.UserRoleDeptPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleDeptRepository extends JpaRepository<UserRoleDept, UserRoleDeptPK> {

}
