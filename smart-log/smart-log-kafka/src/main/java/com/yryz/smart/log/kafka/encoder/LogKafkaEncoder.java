package com.yryz.smart.log.kafka.encoder;

import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;

/**
 * Copyright (c) 2017-2018 Wuhan Yryz Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2017/12/12 18:11
 * Created by lifan
 */
public interface LogKafkaEncoder<E> extends ContextAware, LifeCycle {

    /**
     * kafka消息编码
     *
     * @param event kafka消息
     * @return
     */
    byte[] doEncode(E event);

}
