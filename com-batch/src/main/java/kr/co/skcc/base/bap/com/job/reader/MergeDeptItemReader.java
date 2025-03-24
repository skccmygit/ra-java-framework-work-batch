package kr.co.skcc.base.bap.com.job.reader;


import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.base.com.common.domain.dept.DeptTemp;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.util.HashMap;
import java.util.Map;


public class MergeDeptItemReader extends JpaPagingItemReader<DeptTemp> {

    private final static int CHUNK_SIZE = 500;

    public MergeDeptItemReader(EntityManagerFactory entityManagerFactory) {

        String query = "SELECT t FROM DeptTemp t WHERE t.deptcd <> '' AND t.deptcd IS NOT NULL";
        this.setQueryString(query);
        this.setPageSize(CHUNK_SIZE);
        this.setEntityManagerFactory(entityManagerFactory);
        this.setName("MergeDeptItemReader");
        Map<String, Object> map = new HashMap<>();
        this.setParameterValues(map);
    }
}