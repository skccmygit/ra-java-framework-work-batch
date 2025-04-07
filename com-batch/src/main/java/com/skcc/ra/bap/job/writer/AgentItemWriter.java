package com.skcc.ra.bap.job.writer;

import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.account.domain.auth.Agent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JpaItemWriter;

@Slf4j
public class AgentItemWriter extends JpaItemWriter<Agent> {

    public AgentItemWriter(EntityManagerFactory entityManagerFactory) {
        this.setEntityManagerFactory(entityManagerFactory);
    }
}