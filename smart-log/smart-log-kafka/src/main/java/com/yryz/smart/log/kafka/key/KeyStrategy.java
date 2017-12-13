package com.yryz.smart.log.kafka.key;


/**
 * Copyright (c) 2017-2018 Wuhan Yryz Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2017/12/12 18:21
 * Created by lifan
 * kafka消息发送key生成策略接口
 */
public interface KeyStrategy<E> {

    /**
     * 生成key
     *
     * @param e
     * @return
     */
    byte[] createKey(E e);

}
