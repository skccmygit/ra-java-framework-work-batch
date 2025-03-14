package kr.co.skcc.oss.bap.com.service;


import kr.co.skcc.oss.bap.com.domain.ApiTrace;
import kr.co.skcc.oss.bap.com.repository.ApiInfoRepository;
import kr.co.skcc.oss.bap.com.repository.ApiMonitorRepository;
import kr.co.skcc.oss.com.common.api.dto.domainDto.ApiInfoDto;
import kr.co.skcc.oss.com.common.domain.apiInfo.ApiMonitor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class ApiTraceConsumer {

    @Autowired
    private ApiInfoRepository apiInfoRepository;

    @Autowired
    private ApiMonitorRepository apiMonitorRepository;

    private static final String[] excludedUris = {""};

    private static List<ApiInfoDto> apiInfoDtos = new ArrayList<>();

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    @Transactional(value="transactionManager")
    @KafkaListener(topics = "${kafka.topic.API-TRACE-LOG}", groupId = "${kafka.group-id.API-TRACE-LOG}"
            , concurrency = "${kafka.concurrency.API-TRACE-LOG}", containerFactory = "apiTraceLogListenerContainerFactory"
            , errorHandler = "kafkaErrorHandler")
    public void consume(ConsumerRecords<String, ApiTrace> apiTraces, Acknowledgment ack) {

        log.info("api size : {} ",  apiTraces.count());
        Iterator<ConsumerRecord<String, ApiTrace>> it = apiTraces.iterator();
        while (it.hasNext()) {
            ApiTrace apiTrace = it.next().value();
            // 예외 URL 제외
            if (!Arrays.stream(excludedUris).anyMatch(e -> e.startsWith(apiTrace.getUri()))) {

                log.info("api apiId : {} / {} ",  apiTrace.getMethod(), apiTrace.getUri());
                // API_ID 찾기
                int apiId = apiInfoRepository.findByHttMethodValAndApiLocUrladdr(apiTrace.getMethod(), apiTrace.getUri());

                // 패턴 매칭 찾기
                if (apiId < 0) {
                    synchronized (ApiTraceConsumer.class) {
                        if (apiInfoDtos.size() == 0) {
                            List<Map<String, Object>> apiPaths = apiInfoRepository.findWithPathVariables();
                            for (Map apiPath : apiPaths) {
                                ApiInfoDto apiInfoDto = new ApiInfoDto();
                                apiInfoDto.setApiId(((Integer) apiPath.get("apiId")).intValue());
                                apiInfoDto.setHttMethodVal((String) apiPath.get("httMethodVal"));
                                apiInfoDto.setApiLocUrladdr((String) apiPath.get("apiLocUrladdr"));
                                apiInfoDtos.add(apiInfoDto);
                            }
                        }
                    }

                    for (ApiInfoDto apiInfoDto : apiInfoDtos) {
                        // 패턴 매칭 : URL Path Variable이 있는 경우
                        if (apiInfoDto.getHttMethodVal().equals(apiTrace.getMethod()) &&
                                pathMatcher.match(apiInfoDto.getApiLocUrladdr(), apiTrace.getUri())) {
                            apiId = apiInfoDto.getApiId();
                            log.debug("apiInfoDto={}", apiInfoDto);
                            break;
                        }
                    }
                }

                if (apiId > 0) {
                    ApiMonitor apiMonitor = new ApiMonitor();
                    apiMonitor.setLastChngrId("onm-batch");
                    apiMonitor.setLastChngDtmd(LocalDateTime.now());
                    apiMonitor.setApiExctStartDtmt(apiTrace.getTimestamp());
                    apiMonitor.setApiRespTime(apiTrace.getTimeTaken());
                    apiMonitor.setApiId(apiId);
                    apiMonitor.setApiRespStsVal(apiTrace.getStatus());
                    apiMonitor.setErrCntnt(String.join("", apiTrace.getError()));

                    String userid = apiTrace.getUserId() == null ? "" :
                            apiTrace.getUserId().length() > 8 ?  apiTrace.getUserId().substring(0, 7) : apiTrace.getUserId();

                    apiMonitor.setApiExectUserId(userid);
                    apiMonitor.setConnIpaddr(apiTrace.getRemoteAddress());

                    log.debug("apiMonitor={},{},{}", apiMonitor.getApiId(), apiTrace.getMethod(), apiTrace.getUri());
                    apiMonitorRepository.save(apiMonitor);

                }
            }
        }
        ack.acknowledge();
    }

}