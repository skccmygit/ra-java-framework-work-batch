package kr.co.skcc.oss.bap.com.job.writer;

import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.oss.com.account.domain.hist.UserAuthReqHis;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAuthReqHisItemListWriter extends JpaItemListWriter<UserAuthReqHis> {

    public UserAuthReqHisItemListWriter(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }
}