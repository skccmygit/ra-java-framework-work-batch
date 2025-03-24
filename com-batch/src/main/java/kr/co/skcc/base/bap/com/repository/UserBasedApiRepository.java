package kr.co.skcc.base.bap.com.repository;


import kr.co.skcc.base.bap.com.domain.UserBasedApi;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * UserBasedApi.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-07, 최초 작성
 */
@Repository
public interface UserBasedApiRepository extends CrudRepository<UserBasedApi, UUID> {

    void deleteAllByStatus(String status);
    Iterable<? extends UserBasedApi> findAllByStatus(String status);
}
