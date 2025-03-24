package kr.co.skcc.base.bap.scheduler.domain;

import kr.co.skcc.base.bap.scheduler.domain.type.JobParamType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "OOM10311", catalog = "OOM")
public class JobParameter implements Serializable {

    // 배치작업파라미터순번
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DSPSN_WORK_PARAM_SEQ")
    private Long jobParamSeq;

    // 배치작업파라미터명
    @Column(name = "DSPSN_WORK_PARAM_NM")
    private String jobParamName;

    // 배치작업파라미터유형값
    @Column(name = "DSPSN_WORK_PARAM_TYPE_VAL")
    @Enumerated(EnumType.STRING)
    private JobParamType jobParamType;

    // 배치작업파라미터값
    @Column(name = "DSPSN_WORK_PARAM_VAL")
    private String jobParamValue;

    // 최종변경자ID
    @Column(name = "LAST_CHNGR_ID")
    private String lastModifiedBy;

    // 최종변경일시D
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "LAST_CHNG_DTMD")
    private LocalDateTime lastModifiedAt = LocalDateTime.now();
}