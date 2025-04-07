package com.skcc.ra.bap.repository;

import com.skcc.ra.account.domain.auth.UserMenu;
import com.skcc.ra.account.domain.auth.pk.UserMenuPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMenuRepository extends JpaRepository<UserMenu, UserMenuPK> {

    List<UserMenu> findByUserid(String userid);

    void deleteByUserid(String userid);
}
