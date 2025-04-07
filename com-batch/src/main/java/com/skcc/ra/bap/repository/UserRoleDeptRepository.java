package com.skcc.ra.bap.repository;

import com.skcc.ra.account.domain.baseAuth.UserRoleDept;
import com.skcc.ra.account.domain.baseAuth.pk.UserRoleDeptPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleDeptRepository extends JpaRepository<UserRoleDept, UserRoleDeptPK> {

}
