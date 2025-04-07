package com.skcc.ra.bap.job.reader;


import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.account.domain.auth.Agent;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;


public class AgentEndItemReader extends JpaPagingItemReader<Agent> {

    private final static int CHUNK_SIZE = 5;

    public AgentEndItemReader(EntityManagerFactory entityManagerFactory) {

        String query = "SELECT A.AGENT_REG_SEQ                   \n" +
                    "         ,A.LAST_CHNGR_ID                  \n" +
                    "         ,A.LAST_CHNG_DTMD              \n" +
                    "         ,A.AGENT_ID                            \n" +
                    "         ,A.USERID                             \n" +
                    "         ,A.DEPTCD                                \n" +
                    "         ,A.AGENT_START_DT               \n" +
                    "         ,A.AGENT_END_SCHDL_DT         \n" +
                    "         ,A.AGENT_END_DT                    \n" +
                    "         ,A.END_YN                               \n" +
                    "         ,A.AGENT_REG_RESON_CNTNT    \n" +
                    "    FROM OCO.OCO10133 A                                    \n" +
                    "   WHERE 1=1                                               \n" +
                    "     AND A.END_YN = 'N'                                    \n" +
                    "     AND A.AGENT_END_SCHDL_DT = DATE_FORMAT(NOW() - INTERVAL 1 DAY,'%Y%m%d') \n";

        JpaNativeQueryProvider<Agent> queryProvider = new JpaNativeQueryProvider<>();
        queryProvider.setSqlQuery(query);
        queryProvider.setEntityClass(Agent.class);

        this.setQueryProvider(queryProvider);
        this.setPageSize(CHUNK_SIZE);
        this.setEntityManagerFactory(entityManagerFactory);
        this.setSaveState(false);
    }
}