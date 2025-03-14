package kr.co.skcc.oss.bap.com.job.writer;

import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.oss.com.common.domain.menu.MenuStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JpaItemWriter;

@Slf4j
public class MenuStatisticsItemWriter extends JpaItemWriter<MenuStatistics> {

    public MenuStatisticsItemWriter(EntityManagerFactory entityManagerFactory) {
        this.setEntityManagerFactory(entityManagerFactory);
    }
}