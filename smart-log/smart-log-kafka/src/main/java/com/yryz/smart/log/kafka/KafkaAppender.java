package com.yryz.smart.log.kafka;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * kafka日志Appender
 */
public class KafkaAppender extends KafkaAppenderBase<ILoggingEvent> {
    //创建线程池
    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    //话题
    private String topic;
    //kafka服务器
    private String bootstrapServers;
    //kafka生产者模板
    private KafkaTemplate<byte[], byte[]> kafkaTemplate;

    /**
     * 验证参数
     *
     * @return
     */
    private boolean checkRequiredParam() {
        if (null == this.encoder)
            return false;
        if (null == bootstrapServers || "".equals(bootstrapServers.trim()))
            return false;
        return true;
    }

    @Override
    public void start() {
        if (checkRequiredParam()) {
            Map<String, Object> props = new HashMap<>();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers.trim());
            props.put(ProducerConfig.RETRIES_CONFIG, 0);
            props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
            props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
            props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
            ProducerFactory<byte[], byte[]> producerFactory = new DefaultKafkaProducerFactory<>(props);
            this.kafkaTemplate = new KafkaTemplate<>(producerFactory);
            this.kafkaTemplate.setDefaultTopic(this.topic);
            super.start();
        }
    }

    @Override
    public void stop() {
        super.stop();
        executorService.shutdown();
    }

    @Override
    protected void append(ILoggingEvent event) {
        executorService.execute(() -> this.kafkaTemplate.sendDefault(this.encoder.doEncode(event)));
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

}
