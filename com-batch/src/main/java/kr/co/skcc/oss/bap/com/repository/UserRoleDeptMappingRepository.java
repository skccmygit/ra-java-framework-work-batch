package kr.co.skcc.oss.bap.com.repository;

import kr.co.skcc.oss.com.account.domain.baseAuth.UserRoleDeptMapping;
import kr.co.skcc.oss.com.account.domain.baseAuth.pk.UserRoleDeptMappingPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleDeptMappingRepository extends JpaRepository<UserRoleDeptMapping, UserRoleDeptMappingPK> {

    List<UserRoleDeptMapping> findByRoleDeptTeamCdAndRoleMappReofoCd(String roleDeptTeamCd, String roleMappReofoCd);
}
