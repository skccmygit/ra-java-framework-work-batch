package kr.co.skcc.oss.bap.com.repository;

import kr.co.skcc.oss.com.common.domain.menu.MenuStatistics;
import kr.co.skcc.oss.com.common.domain.menu.pk.MenuStatisticsPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuStatisticsRepository extends JpaRepository<MenuStatistics, MenuStatisticsPK> {

}
