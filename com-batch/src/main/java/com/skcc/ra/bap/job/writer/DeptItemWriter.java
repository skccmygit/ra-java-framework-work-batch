package com.skcc.ra.bap.job.writer;

import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.common.domain.dept.Dept;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JpaItemWriter;

@Slf4j
public class DeptItemWriter extends JpaItemWriter<Dept> {

    public DeptItemWriter(EntityManagerFactory entityManagerFactory) {
        this.setEntityManagerFactory(entityManagerFactory);
    }
}