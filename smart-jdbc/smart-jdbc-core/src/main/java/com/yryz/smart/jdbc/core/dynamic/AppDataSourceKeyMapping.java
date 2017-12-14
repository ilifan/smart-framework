package com.yryz.smart.jdbc.core.dynamic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (c) 2017-2018 Wuhan Yryz Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2017/12/14 15:19
 * Created by lifan
 */
public class AppDataSourceKeyMapping {

    private static final Map<String, String> APP_DATASOURCE_KEY_MAPPING = new ConcurrentHashMap<>();

    static {
        APP_DATASOURCE_KEY_MAPPING.put("1", "SGT");
        APP_DATASOURCE_KEY_MAPPING.put("2", "QUANHU");
        APP_DATASOURCE_KEY_MAPPING.put("3", "ZGZY");
    }

    public static String getDataSourceKeyByAppId(String appId) {
        return APP_DATASOURCE_KEY_MAPPING.get(appId);
    }
}
