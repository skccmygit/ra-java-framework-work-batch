package kr.co.skcc.oss.bap.com.repository;

import kr.co.skcc.oss.bap.com.domain.RoleBasedApi;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * RoleBasedApiRepository.java
 * : 작성필요  authorization
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-07, 최초 작성
 */
@Repository
public interface RoleBasedApiRepository extends CrudRepository<RoleBasedApi, UUID> {

    void deleteAllByStatus(String status);
    Iterable<? extends RoleBasedApi> findAllByStatus(String status);
}
