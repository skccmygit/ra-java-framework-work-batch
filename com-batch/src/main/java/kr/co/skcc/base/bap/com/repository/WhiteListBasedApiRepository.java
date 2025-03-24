package kr.co.skcc.base.bap.com.repository;


import kr.co.skcc.base.bap.com.domain.WhiteListBasedApi;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * WhiteListBasedApi.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-07, 최초 작성
 */
@Repository
public interface WhiteListBasedApiRepository extends CrudRepository<WhiteListBasedApi, Integer> {

    Iterable<? extends WhiteListBasedApi> findAllByStatus(String status);
}
