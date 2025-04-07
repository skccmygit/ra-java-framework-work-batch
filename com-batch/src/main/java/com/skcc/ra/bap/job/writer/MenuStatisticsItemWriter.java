package com.skcc.ra.bap.job.writer;

import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.common.domain.menu.MenuStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JpaItemWriter;

@Slf4j
public class MenuStatisticsItemWriter extends JpaItemWriter<MenuStatistics> {

    public MenuStatisticsItemWriter(EntityManagerFactory entityManagerFactory) {
        this.setEntityManagerFactory(entityManagerFactory);
    }
}