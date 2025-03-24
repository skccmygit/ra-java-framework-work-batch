package kr.co.skcc.base.com.common.domain.apiInfo.pk;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * SQL Trace 로그 객체 PK
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiMonitorPK implements Serializable {

    @Column(name = "API_EXECT_DTL_SEQ")
    private Long apiExectDtlSeq;

    @Column(name = "API_EXECT_START_DTMT")
    private LocalDateTime apiExctStartDtmt;

}