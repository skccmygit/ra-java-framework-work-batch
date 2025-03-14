package kr.co.skcc.oss.bap.com.job.reader;


import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.oss.com.common.domain.userBasic.UserBasic;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.util.HashMap;
import java.util.Map;


public class CapAccountCreationItemReader extends JpaPagingItemReader<UserBasic> {

    private final static int CHUNK_SIZE = 5;

    @Override
    public int getPage() {
        return 0;
    }

    public CapAccountCreationItemReader(EntityManagerFactory entityManagerFactory) {
        // 부서&퇴사 여부 확인 후,
        String query = "SELECT DISTINCT t.empno, t.empKrnNm, t.mphno, t.deptcd, t.reofoCd, t.vctnCd, 'Y' \n" +
                       "  FROM UserBasic t \n" +
                       " WHERE t.empno NOT IN (SELECT k.userIdentNo FROM Account k ) \n" +
                       "   AND t.reofoCd >= '112' AND t.reofoCd <= '210' ";
        this.setQueryString(query);
        this.setPageSize(CHUNK_SIZE);
        this.setEntityManagerFactory(entityManagerFactory);
        this.setName("accountCreationItemReader");
        Map<String, Object> map = new HashMap<>();
        //부서 리스트는 OCO10111 테이블에서 DISTINCT해서 가져올 예정 -> 하드코딩 삭제
        this.setParameterValues(map);
    }
}