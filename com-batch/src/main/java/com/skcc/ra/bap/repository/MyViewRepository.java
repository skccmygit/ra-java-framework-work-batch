package com.skcc.ra.bap.repository;

import com.skcc.ra.account.domain.userSpecificMenu.MyView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyViewRepository extends JpaRepository<MyView, Integer> {
    void deleteByUserid(String userid);
}
