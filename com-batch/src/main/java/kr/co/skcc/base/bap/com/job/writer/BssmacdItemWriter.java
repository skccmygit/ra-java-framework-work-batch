package kr.co.skcc.base.bap.com.job.writer;

import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.base.com.common.domain.dept.Bssmacd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JpaItemWriter;

@Slf4j
public class BssmacdItemWriter extends JpaItemWriter<Bssmacd> {

    public BssmacdItemWriter(EntityManagerFactory entityManagerFactory) {
        this.setEntityManagerFactory(entityManagerFactory);
    }
}