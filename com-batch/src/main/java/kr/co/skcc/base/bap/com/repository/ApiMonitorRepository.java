package kr.co.skcc.base.bap.com.repository;

import kr.co.skcc.base.com.common.domain.apiInfo.ApiMonitor;
import kr.co.skcc.base.com.common.domain.apiInfo.pk.ApiMonitorPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface ApiMonitorRepository extends JpaRepository<ApiMonitor, ApiMonitorPK> {
}
