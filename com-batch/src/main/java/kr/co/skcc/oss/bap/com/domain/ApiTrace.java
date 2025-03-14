package kr.co.skcc.oss.bap.com.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * API Trace 로그 객체
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiTrace {

    /**
     * 로그 일시
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;

    /**
     * 메소스
     */
    private String method;

    /**
     * URI
     */
    private String uri;

    /**
     * 접속IP주소
     */
    private String remoteAddress;

    /**
     * HTTP응답코드
     */
    private int status;

    /**
     * 응답시간
     */
    private int timeTaken;

    /**
     * 에러 메시지
     */
    private String[] error;

    /**
     * 접속사용자ID
     */
    private String userId;
}