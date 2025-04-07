package com.skcc.ra.scheduler.domain;

import com.skcc.ra.scheduler.domain.type.JobInfoStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "OOM10310", catalog = "OOM")
public class SchedulerJobInfo implements Serializable {

    // 배치작업ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DSPSN_WORK_ID")
    private Long jobId;

    // 배치작업명
    @Column(name = "DSPSN_WORK_NM")
    private String jobName;

    // 응용프로그램그룹ID
    @Column(name = "APRO_GROUP_ID")
    private String jobGroup;

    // 배치작업상태명
    @Column(name = "DSPSN_WORK_STS_NM")
    @Enumerated(EnumType.STRING)
    private JobInfoStatus jobInfoStatus;

    // 배치클래스명
    @Column(name = "DSPSN_CLS_NM")
    private String jobClass;

    // 배치스케줄값
    @Column(name = "DSPSN_SCHD_VAL")
    private String cronExpression;

    // 배치작업설명
    @Column(name = "DSPSN_WORK_DESC")
    private String description;

    // 시작일시D
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializers.DateDeserializer.class)
    @Column(name = "START_DTMD")
    private Date startTime;

    // 실행주기값
    @Column(name = "EXCUT_CYCLE_VAL")
    private long repeatInterval;

    // 반복횟수
    @Column(name = "RPTT_FRQY")
    private int repeatCount = 0;

    // 반복수행여부
    @Column(name = "RPTT_EXECT_YN")
    private Boolean cronJob;

    // 최종변경자ID
    @Column(name = "LAST_CHNGR_ID")
    private String lastModifiedBy;

    // 최종변경일시D
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(name = "LAST_CHNG_DTMD")
    private LocalDateTime lastModifiedAt = LocalDateTime.now();

    // 배치작업파라미터
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "DSPSN_WORK_ID", referencedColumnName = "DSPSN_WORK_ID", insertable = true, updatable = true, nullable = false)
    private List<JobParameter> jobParameters;
}
