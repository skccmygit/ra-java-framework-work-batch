package com.skcc.ra.bap.repository;

import com.skcc.ra.account.domain.auth.UserRole;
import com.skcc.ra.account.domain.auth.pk.UserRolePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRolePK> {

    void deleteByUserid(String userid);

    List<UserRole> findByUserid(String userid);
}
