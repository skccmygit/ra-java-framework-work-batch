package kr.co.skcc.base.bap.com.repository;

import kr.co.skcc.base.com.account.domain.userSpecificMenu.MyView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyViewRepository extends JpaRepository<MyView, Integer> {
    void deleteByUserid(String userid);
}
