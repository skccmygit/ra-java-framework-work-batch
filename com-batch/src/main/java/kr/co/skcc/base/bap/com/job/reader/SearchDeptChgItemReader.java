package kr.co.skcc.base.bap.com.job.reader;


import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.base.com.common.domain.userBasic.UserBasic;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.util.HashMap;
import java.util.Map;


public class SearchDeptChgItemReader extends JpaPagingItemReader<UserBasic> {

    private final static int CHUNK_SIZE = 5;

    @Override
    public int getPage() {
        return 0;
    }

    public SearchDeptChgItemReader(EntityManagerFactory entityManagerFactory) {

        //Z: 자동 역할 생성 부서, X: 기본 역할 생성 부서
        String query = "SELECT e.userid, t.deptcd, t.reofoCd \n" +
                       "     , (CASE WHEN b.roleMappReofoCd IS NOT NULL THEN 'Y' ELSE 'X' END) \n" +
                       "     , (CASE WHEN t.deptcd <> e.deptcd  THEN 'Y' ELSE 'X' END) \n" +
                       "     , (CASE WHEN t.reofoCd <> e.reofoCd THEN 'Y' ELSE 'X' END) \n" +
                       "     , t.vctnCd \n " +
                       "  FROM UserBasic t \n" +
                       " INNER JOIN Account e ON e.userIdentNo = t.empno \n" +
                       "                     AND (t.deptcd <> e.deptcd OR t.reofoCd <> e.reofoCd) \n" +
//                       "                     AND e.userid NOT LIKE '%A' \n" +
                       "  LEFT OUTER JOIN UserRoleDeptMapping b ON (b.roleDeptTeamCd = t.deptcd   \n" +
                       "                                        AND b.roleMappReofoCd = t.reofoCd) \n" +
                       " WHERE e.useridStsCd <> 'D' \n";
        this.setQueryString(query);
        this.setPageSize(CHUNK_SIZE);
        this.setEntityManagerFactory(entityManagerFactory);
        this.setName("accountCreationItemReader");
        Map<String, Object> map = new HashMap<>();

        this.setParameterValues(map);
    }
}