package com.skcc.ra.bap.repository;

import com.skcc.ra.account.domain.hist.UserMenuHist;
import com.skcc.ra.account.domain.hist.pk.UserMenuHistPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMenuHistRepository extends JpaRepository<UserMenuHist, UserMenuHistPK> {
}
