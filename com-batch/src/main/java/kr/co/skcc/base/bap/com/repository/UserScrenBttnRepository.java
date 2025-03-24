package kr.co.skcc.base.bap.com.repository;

import kr.co.skcc.base.com.account.domain.auth.UserScrenBttn;
import kr.co.skcc.base.com.account.domain.auth.pk.UserScrenBttnPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserScrenBttnRepository extends JpaRepository<UserScrenBttn, UserScrenBttnPK> {

    List<UserScrenBttn> findByUserid(String userid);

    void deleteByUserid(String userid);
}
