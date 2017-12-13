package com.yryz.smart.log.kafka;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.yryz.smart.log.kafka.key.DefaultKeyStrategy;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright (c) 2017-2018 Wuhan Yryz Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2017/12/12 18:21
 * Created by lifan
 * kafka日志Appender
 */
public class KafkaAppender extends KafkaAppenderBase<ILoggingEvent> {
    //创建线程池
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    //kafka生产者模板
    private KafkaTemplate<byte[], byte[]> kafkaTemplate;

    @Override
    public void start() {
        if (null == this.keyStrategy) {
            this.keyStrategy = new DefaultKeyStrategy();
        }
        this.addProducerConfigValue(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        this.addProducerConfigValue(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        ProducerFactory<byte[], byte[]> producerFactory = new DefaultKafkaProducerFactory<>(this.producerConfig);
        this.kafkaTemplate = new KafkaTemplate<>(producerFactory);
        this.kafkaTemplate.setDefaultTopic(this.topic.trim());
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        executorService.shutdown();
    }

    @Override
    protected void append(ILoggingEvent event) {
        executorService.execute(() -> {
            final byte[] payload = this.encoder.doEncode(event);
            final byte[] key = this.keyStrategy.createKey(event);
            this.kafkaTemplate.sendDefault(key, payload);
        });
    }

}
