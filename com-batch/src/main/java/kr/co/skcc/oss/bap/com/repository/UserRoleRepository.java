package kr.co.skcc.oss.bap.com.repository;

import kr.co.skcc.oss.com.account.domain.auth.UserRole;
import kr.co.skcc.oss.com.account.domain.auth.pk.UserRolePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRolePK> {

    void deleteByUserid(String userid);

    List<UserRole> findByUserid(String userid);
}
