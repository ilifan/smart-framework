package com.yryz.smart.log.kafka.key;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Copyright (c) 2017-2018 Wuhan Yryz Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2017/12/13 14:47
 * Created by lifan
 */
public class DefaultKeyStrategy implements KeyStrategy<ILoggingEvent> {

    @Override
    public byte[] createKey(ILoggingEvent iLoggingEvent) {
        return null;
    }
}
