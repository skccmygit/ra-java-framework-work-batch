package com.skcc.ra.bap.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skcc.ra.bap.domain.ApiTrace;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Slf4j
@Component
public class KafkaErrorHandler implements KafkaListenerErrorHandler {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.API-TRACE-LOG:ONM-T-LOG-API}")
    private String API_TRACE_LOG;

    @Value("${kafka.topic.API-TRACE-LOG-ERR:ONM-T-LOG-API-ERR}")
    private String API_TRACE_LOG_ERR;


    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        log.error("kafkaMessage={}", message.getPayload());
        log.error("errorMessage={}", exception.getMessage());
        return null;
    }

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
        ConsumerRecords<String, ApiTrace> records = (ConsumerRecords<String, ApiTrace>) message.getPayload();
        Iterator<ConsumerRecord<String, ApiTrace>> it = records.iterator();
        exception.printStackTrace();

        while (it.hasNext()) {
            ConsumerRecord<String, ApiTrace> record = it.next();
            log.error("!!!!!ConsumerRecord={},{},{},{},{},{}", record.topic(),
                    record.partition(),
                    record.offset(),
                    record.key(),
                    record.value(),
                    record.timestamp());

            ApiTrace apiTrace = record.value();
            ProducerRecord<String, String> produce = null;
            try {
                // 에러발생 시 관련 API는 무조건 에러 토픽에 넣어놓는다
//                if (apiTrace.getError().length != 0) {
                    produce = new ProducerRecord<>(API_TRACE_LOG_ERR, mapper.writeValueAsString(record.value()));
//                }
//                else {
//                    produce = new ProducerRecord<>(API_TRACE_LOG, mapper.writeValueAsString(record.value()));
//                }
            }
            catch (JsonProcessingException e) {
//                e.printStackTrace();
                log.error(e.getMessage(), e);
                consumer.commitAsync();
            }
            kafkaTemplate.send(produce);
        }
        return null;
    }
}
