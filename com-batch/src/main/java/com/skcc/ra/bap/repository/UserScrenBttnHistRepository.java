package com.skcc.ra.bap.repository;

import com.skcc.ra.account.domain.hist.UserScrenBttnHist;
import com.skcc.ra.account.domain.hist.pk.UserScrenBttnHistPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserScrenBttnHistRepository extends JpaRepository<UserScrenBttnHist, UserScrenBttnHistPK> {

}
