package kr.co.skcc.base.bap.com.repository;

import kr.co.skcc.base.com.common.domain.menu.MenuStatistics;
import kr.co.skcc.base.com.common.domain.menu.pk.MenuStatisticsPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuStatisticsRepository extends JpaRepository<MenuStatistics, MenuStatisticsPK> {

}
