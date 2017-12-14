package com.yryz.smart.jdbc.core.dynamic;

import org.springframework.util.Assert;

/**
 * Copyright (c) 2017-2018 Wuhan Yryz Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2017/12/14 15:28
 * Created by lifan
 */
public class DynamicDataSourceHolder {
    // 线程内的数据源KEY
    private static final ThreadLocal<String> DATASOURCE_KEY = new ThreadLocal<>();

    public static void set(String appId) {
        Assert.hasText(appId, "appId cannot be null");
        String dataSourceKey = AppDataSourceKeyMapping.getDataSourceKeyByAppId(appId);
        Assert.hasText(appId, "appId【" + appId + "】 have not set a datasource key");
        DATASOURCE_KEY.set(dataSourceKey);
    }

    public static String get() {
        return DATASOURCE_KEY.get();
    }

    public static void clear() {
        DATASOURCE_KEY.remove();
    }
}
