package com.skcc.ra.bap.repository;

import jakarta.persistence.QueryHint;
import com.skcc.ra.account.api.dto.domainDto.AccountDto;
import com.skcc.ra.account.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import static org.hibernate.annotations.QueryHints.COMMENT;

public interface AccountRepository extends JpaRepository<Account, String> {

    @QueryHints(@QueryHint(name = COMMENT, value = "공통, common, 나태관"))
    @Query(value = "SELECT new com.skcc.ra.account.api.dto.domainDto.AccountDto(t.userid, t.deptcd, d.deptNm) " +
            "FROM Account t " +
            "LEFT OUTER JOIN Dept d ON t.deptcd = d.deptcd " +
            "WHERE t.userid = :userid ", nativeQuery = true)
    AccountDto findUserDeptInfo(String userid);

}
