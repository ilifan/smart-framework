package com.yryz.smart.log.kafka;

import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.yryz.smart.log.kafka.encoder.LogKafkaEncoder;
import com.yryz.smart.log.kafka.key.KeyStrategy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

/**
 * Copyright (c) 2017-2018 Wuhan Yryz Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2017/12/12 18:21
 * Created by lifan
 */
public abstract class KafkaAppenderBase<E> extends UnsynchronizedAppenderBase<E> {
    protected String topic;
    protected LogKafkaEncoder<E> encoder = null;
    protected KeyStrategy<E> keyStrategy = null;
    protected Map<String, Object> producerConfig = new HashMap<>();

    private static final Set<String> PRODUCER_CONFIG_KEYS = new HashSet<>();

    static {
        PRODUCER_CONFIG_KEYS.add(BOOTSTRAP_SERVERS_CONFIG);
        PRODUCER_CONFIG_KEYS.add(METADATA_MAX_AGE_CONFIG);
        PRODUCER_CONFIG_KEYS.add(BATCH_SIZE_CONFIG);
        PRODUCER_CONFIG_KEYS.add(ACKS_CONFIG);
        PRODUCER_CONFIG_KEYS.add(LINGER_MS_CONFIG);
        PRODUCER_CONFIG_KEYS.add(CLIENT_ID_CONFIG);
        PRODUCER_CONFIG_KEYS.add(SEND_BUFFER_CONFIG);
        PRODUCER_CONFIG_KEYS.add(RECEIVE_BUFFER_CONFIG);
        PRODUCER_CONFIG_KEYS.add(MAX_REQUEST_SIZE_CONFIG);
        PRODUCER_CONFIG_KEYS.add(RECONNECT_BACKOFF_MS_CONFIG);
        PRODUCER_CONFIG_KEYS.add(RECONNECT_BACKOFF_MAX_MS_CONFIG);
        PRODUCER_CONFIG_KEYS.add(MAX_BLOCK_MS_CONFIG);
        PRODUCER_CONFIG_KEYS.add(BUFFER_MEMORY_CONFIG);
        PRODUCER_CONFIG_KEYS.add(RETRY_BACKOFF_MS_CONFIG);
        PRODUCER_CONFIG_KEYS.add(COMPRESSION_TYPE_CONFIG);
        PRODUCER_CONFIG_KEYS.add(METRICS_SAMPLE_WINDOW_MS_CONFIG);
        PRODUCER_CONFIG_KEYS.add(METRICS_NUM_SAMPLES_CONFIG);
        PRODUCER_CONFIG_KEYS.add(METRICS_RECORDING_LEVEL_CONFIG);
        PRODUCER_CONFIG_KEYS.add(METRIC_REPORTER_CLASSES_CONFIG);
        PRODUCER_CONFIG_KEYS.add(MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION);
        PRODUCER_CONFIG_KEYS.add(RETRIES_CONFIG);
        PRODUCER_CONFIG_KEYS.add(KEY_SERIALIZER_CLASS_CONFIG);
        PRODUCER_CONFIG_KEYS.add(VALUE_SERIALIZER_CLASS_CONFIG);
        PRODUCER_CONFIG_KEYS.add(CONNECTIONS_MAX_IDLE_MS_CONFIG);
        PRODUCER_CONFIG_KEYS.add(PARTITIONER_CLASS_CONFIG);
        PRODUCER_CONFIG_KEYS.add(REQUEST_TIMEOUT_MS_CONFIG);
        PRODUCER_CONFIG_KEYS.add(INTERCEPTOR_CLASSES_CONFIG);
        PRODUCER_CONFIG_KEYS.add(ENABLE_IDEMPOTENCE_CONFIG);
        PRODUCER_CONFIG_KEYS.add(TRANSACTION_TIMEOUT_CONFIG);
        PRODUCER_CONFIG_KEYS.add(TRANSACTIONAL_ID_CONFIG);
    }

    /**
     * 验证参数
     *
     * @return
     */
    private boolean checkRequiredParam() {
        if (null == this.encoder) {
            addError("No LogKafkaEncoder set for the appender named [\"" + name + "\"].");
            return false;
        }
        if (null == this.topic || "".equals(this.topic.trim())) {
            addError("No Kafka Topic set for the appender named [\"" + name + "\"].");
            return false;
        }
        if (null == producerConfig.get(BOOTSTRAP_SERVERS_CONFIG)) {
            addError("No Kafka \"" + BOOTSTRAP_SERVERS_CONFIG + "\" set for the appender named [\"" + name + "\"].");
            return false;
        }
        return true;
    }

    @Override
    public void start() {
        if (checkRequiredParam()) {
            encoder.start();
            super.start();
        }
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setEncoder(LogKafkaEncoder<E> encoder) {
        this.encoder = encoder;
    }

    public void setKeyStrategy(KeyStrategy<E> keyStrategy) {
        this.keyStrategy = keyStrategy;
    }

    public void addProducerConfig(String keyValue) {
        String[] split = keyValue.split("=", 2);
        if (split.length == 2) {
            addProducerConfigValue(split[0], split[1]);
        }
    }


    protected void addProducerConfigValue(String key, Object value) {
        if (!PRODUCER_CONFIG_KEYS.contains(key)) {
            addWarn("The key \"" + key + "\" is not a kafka producer config key.");
        } else {
            this.producerConfig.put(key, value);
        }
    }
}
