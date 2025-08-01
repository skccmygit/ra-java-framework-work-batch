package com.skcc.ra.bap.repository;

import com.skcc.ra.common.domain.dept.Bssmacd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BssmacdRepository extends JpaRepository<Bssmacd, String> {

    @Query(value = "SELECT COUNT(*) " +
                    "  FROM OCO.OCO50300 " +
                    "  WHERE LAST_CHNG_DTMD > :today", nativeQuery = true)
    Long countTodayUpdate(String today);
}