package com.skcc.ra.bap.repository;

import com.skcc.ra.account.domain.auth.UserScrenBttn;
import com.skcc.ra.account.domain.auth.pk.UserScrenBttnPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserScrenBttnRepository extends JpaRepository<UserScrenBttn, UserScrenBttnPK> {

    List<UserScrenBttn> findByUserid(String userid);

    void deleteByUserid(String userid);
}
