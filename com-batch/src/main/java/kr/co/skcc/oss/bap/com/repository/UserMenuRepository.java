package kr.co.skcc.oss.bap.com.repository;

import kr.co.skcc.oss.com.account.domain.auth.UserMenu;
import kr.co.skcc.oss.com.account.domain.auth.pk.UserMenuPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMenuRepository extends JpaRepository<UserMenu, UserMenuPK> {

    List<UserMenu> findByUserid(String userid);

    void deleteByUserid(String userid);
}
