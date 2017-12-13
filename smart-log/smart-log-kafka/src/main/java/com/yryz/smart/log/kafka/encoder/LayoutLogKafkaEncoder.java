package com.yryz.smart.log.kafka.encoder;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;

import java.nio.charset.Charset;

/**
 * Copyright (c) 2017-2018 Wuhan Yryz Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2017/12/12 18:11
 * Created by lifan
 */
public class LayoutLogKafkaEncoder extends LogKafkaEncoderBase<ILoggingEvent> {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private Layout<ILoggingEvent> layout;
    private Charset charset;

    public LayoutLogKafkaEncoder() {
    }

    public LayoutLogKafkaEncoder(Layout<ILoggingEvent> layout, Charset charset) {
        this.layout = layout;
        this.charset = charset;
    }

    @Override
    public void start() {
        if (charset == null) {
            addInfo("No charset specified for PatternLayoutKafkaEncoder. Using default UTF8 encoding.");
            charset = UTF8;
        }
        super.start();
    }

    @Override
    public byte[] doEncode(ILoggingEvent event) {
        final String message = layout.doLayout(event);
        return message.getBytes(charset);
    }

    public void setLayout(Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Layout<ILoggingEvent> getLayout() {
        return layout;
    }

    public Charset getCharset() {
        return charset;
    }
}
