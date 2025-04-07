package com.skcc.ra.bap.repository;

import com.skcc.ra.common.domain.menu.MenuStatistics;
import com.skcc.ra.common.domain.menu.pk.MenuStatisticsPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuStatisticsRepository extends JpaRepository<MenuStatistics, MenuStatisticsPK> {

}
