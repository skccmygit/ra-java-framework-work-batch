package kr.co.skcc.oss.bap.com.job.writer;

import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.oss.com.account.domain.auth.UserRole;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRoleItemListWriter extends JpaItemListWriter<UserRole> {

    public UserRoleItemListWriter(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }
}