package com.yryz.smart.log.kafka.encoder;

import ch.qos.logback.core.Layout;

import java.nio.charset.Charset;

/**
 * Copyright (c) 2017-2018 Wuhan Yryz Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2017/12/12 18:11
 * Created by lifan
 */
public class LayoutLogKafkaEncoder<E> extends LogKafkaEncoderBase<E> {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private Layout<E> layout;
    private Charset charset;

    public LayoutLogKafkaEncoder() {
    }

    public LayoutLogKafkaEncoder(Layout<E> layout, Charset charset) {
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
    public byte[] doEncode(E event) {
        final String message = layout.doLayout(event);
        return message.getBytes(charset);
    }

    public void setLayout(Layout<E> layout) {
        this.layout = layout;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Layout<E> getLayout() {
        return layout;
    }

    public Charset getCharset() {
        return charset;
    }
}
