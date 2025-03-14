package kr.co.skcc.oss.bap.scheduler.controller.dto;

import kr.co.skcc.oss.bap.scheduler.domain.JobParameter;
import kr.co.skcc.oss.bap.scheduler.domain.SchedulerJobInfo;
import kr.co.skcc.oss.bap.scheduler.domain.type.JobInfoStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerJobDto {

    //배치작업ID
    private Long jobId;

    //배치작업명
    private String jobName;

    //응용프로그램그룹ID
    private String jobGroup;

    //배치작업상태명
    @Enumerated(EnumType.STRING)
    private JobInfoStatus jobInfoStatus;

    //배치클래스명
    private String jobClass;

    //배치스케줄값
    private String cronExpression;

    //배치작업설명
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializers.DateDeserializer.class)
    //시작일시D
    private Date startTime;

    //실행주기값
    private long repeatInterval;

    //반복횟수
    private int repeatCount = 0;

    //반복수행여부
    private Boolean cronJob;

    //최종변경자ID
    private String lastModifiedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    //최종변경일시D
    private LocalDateTime lastModifiedAt = LocalDateTime.now();

    //배치작업파라미터
    private List<JobParameter> jobParameters;

    public SchedulerJobInfo toEntity() {
        SchedulerJobInfo schedulerJobInfo = new SchedulerJobInfo();
        BeanUtils.copyProperties(this, schedulerJobInfo);
        return schedulerJobInfo;
    }
}
