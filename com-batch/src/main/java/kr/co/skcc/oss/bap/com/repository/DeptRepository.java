package kr.co.skcc.oss.bap.com.repository;

import kr.co.skcc.oss.com.common.domain.dept.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeptRepository extends JpaRepository<Dept, String> {

     Dept findByDeptcd(String deptcd);

     @Query(value = "SELECT COUNT(*) " +
             "  FROM OCO.OCO50200 " +
             "  WHERE LAST_CHNG_DTMD > :today", nativeQuery = true)
     Long countTodayUpdate(String today);
}