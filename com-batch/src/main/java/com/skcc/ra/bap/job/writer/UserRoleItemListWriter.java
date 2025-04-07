package com.skcc.ra.bap.job.writer;

import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.account.domain.auth.UserRole;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRoleItemListWriter extends JpaItemListWriter<UserRole> {

    public UserRoleItemListWriter(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }
}