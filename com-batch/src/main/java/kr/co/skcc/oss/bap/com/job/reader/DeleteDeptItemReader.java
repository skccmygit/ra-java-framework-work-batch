package kr.co.skcc.oss.bap.com.job.reader;


import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.oss.com.common.domain.dept.Dept;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class DeleteDeptItemReader extends JpaPagingItemReader<Dept> {

    private final static int CHUNK_SIZE = 500;

    @Override
    public int getPage() {
        return 0;
    }

    public DeleteDeptItemReader(EntityManagerFactory entityManagerFactory) {

        String query = "SELECT t FROM Dept t Where DATE_FORMAT(t.lastChngDtmd, '%Y%m%d') < :resgDt ";
        this.setQueryString(query);
        this.setPageSize(CHUNK_SIZE);
        this.setEntityManagerFactory(entityManagerFactory);
        this.setName("DeleteDeptItemReader");

        Map<String, Object> map = new HashMap<>();
        String regsDt= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        map.put("resgDt",regsDt);
        this.setParameterValues(map);
    }
}