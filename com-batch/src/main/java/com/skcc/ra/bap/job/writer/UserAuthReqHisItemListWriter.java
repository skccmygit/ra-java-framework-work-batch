package com.skcc.ra.bap.job.writer;

import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.account.domain.hist.UserAuthReqHis;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAuthReqHisItemListWriter extends JpaItemListWriter<UserAuthReqHis> {

    public UserAuthReqHisItemListWriter(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }
}