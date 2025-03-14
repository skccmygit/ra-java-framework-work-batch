package kr.co.skcc.oss.bap.com.repository;

import jakarta.persistence.QueryHint;
import kr.co.skcc.oss.com.account.domain.hist.UserMenuHist;
import kr.co.skcc.oss.com.account.domain.hist.pk.UserMenuHistPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import static org.hibernate.annotations.QueryHints.COMMENT;

@Repository
public interface UserMenuHistRepository extends JpaRepository<UserMenuHist, UserMenuHistPK> {
}
