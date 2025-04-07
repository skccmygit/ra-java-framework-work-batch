package com.skcc.ra.bap.repository;

import com.skcc.ra.common.domain.apiInfo.ApiMonitor;
import com.skcc.ra.common.domain.apiInfo.pk.ApiMonitorPK;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface ApiMonitorRepository extends JpaRepository<ApiMonitor, ApiMonitorPK> {
}
