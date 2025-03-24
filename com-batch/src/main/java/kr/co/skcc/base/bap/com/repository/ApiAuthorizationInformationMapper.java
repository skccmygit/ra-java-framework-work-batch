package kr.co.skcc.base.bap.com.repository;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * ApiAuthorizationInformationMapper.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-11, 최초 작성
 */
@Mapper
public interface ApiAuthorizationInformationMapper {

    List<Map<String, Object>> findWhiteListBasedApiUpdateList();

    List<Map<String, Object>> findRoleBasedApiUpdateList();

    List<Map<String, Object>> findUserBasedApiUpdateList();

}
