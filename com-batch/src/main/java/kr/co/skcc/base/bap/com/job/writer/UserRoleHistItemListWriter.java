package kr.co.skcc.base.bap.com.job.writer;

import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.base.com.account.domain.hist.UserRoleHist;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRoleHistItemListWriter extends JpaItemListWriter<UserRoleHist> {

    public UserRoleHistItemListWriter(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
   }
}