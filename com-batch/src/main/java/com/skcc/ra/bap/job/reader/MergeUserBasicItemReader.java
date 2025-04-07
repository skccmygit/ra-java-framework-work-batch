package com.skcc.ra.bap.job.reader;


import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.common.domain.userBasic.UserBasicTemp;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.util.HashMap;
import java.util.Map;


public class MergeUserBasicItemReader extends JpaPagingItemReader<UserBasicTemp> {

    private final static int CHUNK_SIZE = 500;


    public MergeUserBasicItemReader(EntityManagerFactory entityManagerFactory) {

        String query = "SELECT t FROM UserBasicTemp t WHERE t.empno <> '' AND t.empno IS NOT NULL";
        this.setQueryString(query);
        this.setPageSize(CHUNK_SIZE);
        this.setEntityManagerFactory(entityManagerFactory);
        this.setName("MergeUserBasicItemReader");
        Map<String, Object> map = new HashMap<>();
        this.setParameterValues(map);
    }
}