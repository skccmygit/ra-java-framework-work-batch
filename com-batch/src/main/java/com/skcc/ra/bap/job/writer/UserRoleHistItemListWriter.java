package com.skcc.ra.bap.job.writer;

import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.account.domain.hist.UserRoleHist;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRoleHistItemListWriter extends JpaItemListWriter<UserRoleHist> {

    public UserRoleHistItemListWriter(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
   }
}