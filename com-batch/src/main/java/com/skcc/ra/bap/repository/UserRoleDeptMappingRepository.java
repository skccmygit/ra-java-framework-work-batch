package com.skcc.ra.bap.repository;

import com.skcc.ra.account.domain.baseAuth.UserRoleDeptMapping;
import com.skcc.ra.account.domain.baseAuth.pk.UserRoleDeptMappingPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleDeptMappingRepository extends JpaRepository<UserRoleDeptMapping, UserRoleDeptMappingPK> {

    List<UserRoleDeptMapping> findByRoleDeptTeamCdAndRoleMappReofoCd(String roleDeptTeamCd, String roleMappReofoCd);
}
