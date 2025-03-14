package kr.co.skcc.oss.bap.com.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ApiInfoRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int findByHttMethodValAndApiLocUrladdr(String httMethodVal, String apiLocUrladdr) {
        String sql = "SELECT API_ID FROM OCO.OCO40110 WHERE HTT_METHOD_VAL = :method AND API_LOC_URLADDR = :uri";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("method", httMethodVal)
                .addValue("uri", apiLocUrladdr);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return -1;
        }
    }

    public List<Map<String, Object>> findWithPathVariables() {
        String sql = "SELECT API_ID as apiId, HTT_METHOD_VAL as httMethodVal, API_LOC_URLADDR as apiLocUrladdr FROM OCO.OCO40110 WHERE API_LOC_URLADDR LIKE '%{%}%'";
        SqlParameterSource namedParameters = new MapSqlParameterSource();
        try {
            return namedParameterJdbcTemplate.queryForList(sql, namedParameters);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
