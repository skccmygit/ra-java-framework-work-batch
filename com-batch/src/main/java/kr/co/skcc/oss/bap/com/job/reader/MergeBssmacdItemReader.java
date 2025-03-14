package kr.co.skcc.oss.bap.com.job.reader;


import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.oss.com.common.domain.dept.BssmacdTemp;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.util.HashMap;
import java.util.Map;


public class MergeBssmacdItemReader extends JpaPagingItemReader<BssmacdTemp> {

    private final static int CHUNK_SIZE = 500;

    public MergeBssmacdItemReader(EntityManagerFactory entityManagerFactory) {

        String query = "SELECT t FROM BssmacdTemp t";
        this.setQueryString(query);
        this.setPageSize(CHUNK_SIZE);
        this.setEntityManagerFactory(entityManagerFactory);
        this.setName("MergeBssmacdItemReader");
        Map<String, Object> map = new HashMap<>();
        this.setParameterValues(map);
    }
}