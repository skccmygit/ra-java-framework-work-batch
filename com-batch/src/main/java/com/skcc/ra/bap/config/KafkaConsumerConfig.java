package com.skcc.ra.bap.config;

import com.skcc.ra.bap.domain.ApiTrace;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.util.Map;

@EnableKafka
@Component
@Profile("!test")
public class KafkaConsumerConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public ConcurrentKafkaListenerContainerFactory apiTraceLogListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, ApiTrace> factory = new ConcurrentKafkaListenerContainerFactory<String, ApiTrace>();
        factory.setConsumerFactory(apiTraceLogConsumerFactory());
        factory.getContainerProperties().setAckMode(kafkaProperties.getListener().getAckMode());
        factory.setBatchListener(true);

        return factory;
    }

    @Bean
    public ConsumerFactory<String, ApiTrace> apiTraceLogConsumerFactory(){
        return new DefaultKafkaConsumerFactory<>(apiTraceLogConsumerFactoryConfig(),
                new StringDeserializer(),
                apiTraceLogDeserializer());
    }

    private Map<String, Object> apiTraceLogConsumerFactoryConfig() {
        Map<String, Object> consumerConfigs = kafkaProperties.buildConsumerProperties(null);
        return consumerConfigs;
    }

    private JsonDeserializer apiTraceLogDeserializer() {
        JsonDeserializer deserializer = new JsonDeserializer<>(ApiTrace.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);
        deserializer.setUseTypeHeaders(false);
        return deserializer;
    }
}
