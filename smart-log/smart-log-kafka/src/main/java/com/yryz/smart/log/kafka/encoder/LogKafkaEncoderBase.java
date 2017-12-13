package com.yryz.smart.log.kafka.encoder;

import ch.qos.logback.core.spi.ContextAwareBase;

/**
 * Copyright (c) 2017-2018 Wuhan Yryz Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2017/12/12 18:11
 * Created by lifan
 */
public abstract class LogKafkaEncoderBase<E> extends ContextAwareBase implements LogKafkaEncoder<E> {

    private boolean started = false;

    @Override
    public void start() {
        started = true;
    }

    @Override
    public void stop() {
        started = false;
    }

    @Override
    public boolean isStarted() {
        return started;
    }
}
