package kr.co.skcc.base.bap.com.job.writer;

import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.base.com.common.domain.userBasic.UserBasic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JpaItemWriter;

@Slf4j
public class UserBasicItemWriter extends JpaItemWriter<UserBasic> {

    public UserBasicItemWriter(EntityManagerFactory entityManagerFactory) {
        this.setEntityManagerFactory(entityManagerFactory);
    }
}