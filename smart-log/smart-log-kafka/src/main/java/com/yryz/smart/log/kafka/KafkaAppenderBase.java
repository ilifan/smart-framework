package com.yryz.smart.log.kafka;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.yryz.smart.log.kafka.encoder.LogKafkaEncoder;

/**
 * Copyright (c) 2017-2018 Wuhan Yryz Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2017/12/12 18:21
 * Created by lifan
 */
public abstract class KafkaAppenderBase<E> extends AppenderBase<E> {
    protected LogKafkaEncoder<ILoggingEvent> encoder = null;

    @Override
    public void start() {
        if(null != encoder){
            encoder.start();
        }
        super.start();
    }

    public void setEncoder(LogKafkaEncoder<ILoggingEvent> encoder) {
        this.encoder = encoder;
    }
}
